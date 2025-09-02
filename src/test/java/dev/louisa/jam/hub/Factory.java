package dev.louisa.jam.hub;

import dev.louisa.jam.hub.domain.band.BandFactory;
import dev.louisa.jam.hub.domain.band.BandMemberFactory;

public final class Factory {
        public static final BandFactory band = new BandFactory();
        public static final BandMemberFactory bandMember = new BandMemberFactory();

        private Factory() {} // prevent instantiation
}