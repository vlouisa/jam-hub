package dev.louisa.jam.hub.testsupport;

import dev.louisa.jam.hub.application.gig.GigDetailsFactory;
import dev.louisa.jam.hub.domain.band.BandFactory;
import dev.louisa.jam.hub.domain.band.BandMemberFactory;
import dev.louisa.jam.hub.domain.gig.GigFactory;
import dev.louisa.jam.hub.domain.gig.GigRoleAssignmentFactory;
import dev.louisa.jam.hub.domain.registration.UserRegistrationFactory;
import dev.louisa.jam.hub.domain.user.UserFactory;
import dev.louisa.jam.hub.interfaces.gig.GigRequestFactory;

public final class Factory {
    public static final class web {
        // factories for web layer DTOs
        public static final GigRequestFactory aGigRequest = new GigRequestFactory();
        
    }

    public static final class application {
        public static final GigDetailsFactory gigDetails = new GigDetailsFactory();
    }

    public static final class domain {
        // factories for aggregate roots 
        public static final BandFactory aBand = new BandFactory();
        public static final GigFactory aGig = new GigFactory();
        public static final UserRegistrationFactory aUserRegistration = new UserRegistrationFactory();
        public static final UserFactory aUser = new UserFactory();

        // factories for entities and value objects
        public static final BandMemberFactory aBandMember = new BandMemberFactory();
        public static final GigRoleAssignmentFactory aGigRoleAssignment = new GigRoleAssignmentFactory();
    }

    private Factory() {} // prevent instantiation
}