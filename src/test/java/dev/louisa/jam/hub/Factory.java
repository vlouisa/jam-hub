package dev.louisa.jam.hub;

import dev.louisa.jam.hub.domain.band.BandFactory;
import dev.louisa.jam.hub.domain.band.BandMemberFactory;
import dev.louisa.jam.hub.domain.gig.GigFactory;
import dev.louisa.jam.hub.domain.gig.GigRoleAssignmentFactory;

public final class Factory {
        public static final BandFactory bandFactory = new BandFactory();
        public static final BandMemberFactory bandMemberFactory = new BandMemberFactory();
        public static final GigFactory gigFactory = new GigFactory();
        public static final GigRoleAssignmentFactory gigRoleAssignmentFactory = new GigRoleAssignmentFactory();
        
        private Factory() {} // prevent instantiation
}