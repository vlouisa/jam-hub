$baseUrl = "http://localhost:8091"
$breaker = "database-cb"

# --- Config values from your YAML ---
$slidingWindowSize = 60
$minimumNumberOfCalls = 5
$failureRateThreshold = 40
$waitDurationOpenSeconds = 30 
$permittedHalfOpen = 1

# --- State tracking ---
$previousState = ""
$openSince = $null

while ($true) {
    Clear-Host

    $states = @("closed", "open", "half_open")
    $state = "UNKNOWN"
    $emoji = "❔"

    # --- Determine current breaker state ---
    foreach ($s in $states) {
        $url = "$baseUrl/actuator/metrics/resilience4j.circuitbreaker.state?tag=name:$breaker&tag=state:$s"
        $response = curl -s $url | ConvertFrom-Json
        if ($response -and $response.measurements -and $response.measurements[0].value -eq 1) {
            $state = $s.ToUpper()
            switch ($state) {
                "OPEN"      { $emoji = "🔴" }
                "CLOSED"    { $emoji = "🟢" }
                "HALF_OPEN" { $emoji = "🟡" }
            }
        }
    }

    # --- Detect state changes for timing ---
    if ($state -ne $previousState) {
        if ($state -eq "OPEN") {
            $openSince = Get-Date
        }
        $previousState = $state
    }

    # --- Determine which sample size applies for failure rate computation ---
    $currentMinCalls = if ($state -eq "HALF_OPEN") { $permittedHalfOpen } else { $minimumNumberOfCalls }

    # --- Metrics ---
    $fUrl = "$baseUrl/actuator/metrics/resilience4j.circuitbreaker.failure.rate?tag=name:$breaker"
    $fResp = curl -s $fUrl | ConvertFrom-Json
    $f = if ($fResp.measurements) { $fResp.measurements[0].value } else { -1 }

    $bufferedUrl = "$baseUrl/actuator/metrics/resilience4j.circuitbreaker.buffered.calls?tag=name:$breaker"
    $bufferedResp = curl -s $bufferedUrl | ConvertFrom-Json
    $bufferedCalls = if ($bufferedResp.measurements) { $bufferedResp.measurements[0].value } else { 0 }

    # --- Compute text/color for failure rate ---
    if ($f -eq -1) {
        $fText = "Not enough calls (< $currentMinCalls)"
        $fColor = "Yellow"
    } elseif ($f -lt $failureRateThreshold) {
        $fText = "$([math]::Round($f, 2)) %"
        $fColor = "Green"
    } else {
        $fText = "$([math]::Round($f, 2)) %"
        $fColor = "Red"
    }

    # --- Determine per-state values ---
    if ($state -eq "CLOSED") {
        $closedCallsText = "$bufferedCalls / $slidingWindowSize"
        $closedFailureText = $fText
        $halfOpenCallsText = "N/A"
        $halfOpenFailureText = "N/A"
    } elseif ($state -eq "HALF_OPEN") {
        $halfCalls = [Math]::Min($bufferedCalls, $permittedHalfOpen)
        $closedCallsText = "N/A"
        $closedFailureText = "N/A"
        $halfOpenCallsText = "$halfCalls / $permittedHalfOpen"
        $halfOpenFailureText = $fText
    } else {
        # OPEN or other
        $closedCallsText = "N/A"
        $closedFailureText = "N/A"
        $halfOpenCallsText = "N/A"
        $halfOpenFailureText = "N/A"
    }

    # --- Compute open state timing ---
    if ($state -eq "OPEN" -and $openSince) {
        $elapsed = (Get-Date) - $openSince
        $elapsedSec = [math]::Round($elapsed.TotalSeconds, 1)
        $remaining = [math]::Max(0, $waitDurationOpenSeconds - $elapsedSec)
        $elapsedText = "{0:N1}s" -f $elapsedSec
        $remainingText = "{0:N1}s" -f $remaining
    } else {
        $elapsedText = "N/A"
        $remainingText = "N/A"
    }

    # --- Display summary ---
    Write-Host "`n=== Circuit Breaker Status ($breaker) ===" -ForegroundColor Cyan
    Write-Host "State              :`t$emoji $state"
    Write-Host ""

    Write-Host "=========== CLOSED STATE STATS ===========" -ForegroundColor Gray
    Write-Host "Calls              :`t$closedCallsText"
    Write-Host -NoNewline "Failure rate       :`t"
    Write-Host $closedFailureText -ForegroundColor $fColor
    Write-Host "Failure threshold  :`t$failureRateThreshold %"
    Write-Host "Min sample size    :`t$minimumNumberOfCalls calls"
    Write-Host "Sliding window     :`t$slidingWindowSize calls"
    Write-Host ""

    Write-Host "======== HALF OPEN STATE STATS ===========" -ForegroundColor Gray
    Write-Host "Calls              :`t$halfOpenCallsText"
    Write-Host -NoNewline "Failure rate       :`t"
    Write-Host $halfOpenFailureText -ForegroundColor $fColor
    Write-Host "Failure threshold  :`t$failureRateThreshold %"
    Write-Host "Min sample size    :`t$permittedHalfOpen calls"
    Write-Host ""

    Write-Host "========== OPEN STATE STATS =============" -ForegroundColor Gray
    Write-Host "Cool-off duration  :`t$waitDurationOpenSeconds s"
    Write-Host "Elapsed time       :`t$elapsedText"
    Write-Host "Remaining time     :`t$remainingText"
    Write-Host "====================================="
    Write-Host "(Refreshing every 10 seconds...)`n" -ForegroundColor DarkGray

    Start-Sleep -Seconds 10
}
