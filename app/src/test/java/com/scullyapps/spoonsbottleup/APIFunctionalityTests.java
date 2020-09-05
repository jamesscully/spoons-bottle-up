package com.scullyapps.spoonsbottleup;

import com.scullyapps.spoonsbottleup.data.API;
import com.scullyapps.spoonsbottleup.models.Pub;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class APIFunctionalityTests {

    // API code is hidden publically, but can be tested
    @Test
    public void testValidPubIds() {
        assertTrue(API.INSTANCE.isValidPubId(405));
        assertFalse(API.INSTANCE.isValidPubId(-1));
        assertFalse(API.INSTANCE.isValidPubId(9000));
    }

    @Test
    public void testPubScraping() {
        HashMap<Integer, Pub> map = API.INSTANCE.getAllPubs();

        assertEquals("Skegness", map.get(405).getAddress().getTown());
        assertEquals("The Tim Bobbin", map.get(1103).getName());
        assertEquals(true, map.get(7262).getAirport());

    }
}