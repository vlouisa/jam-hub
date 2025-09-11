package dev.louisa.jam.hub.infrastructure.security;

import java.util.List;
import java.util.Map;

public record UnsecuredEndpoints(List<Map.Entry<String, String>> list) {}
