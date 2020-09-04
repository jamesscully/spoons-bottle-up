package com.scullyapps.spoonsbottleup;

import com.scullyapps.spoonsbottleup.data.API;

import org.junit.Test;

import static org.junit.Assert.*;

public class APIFunctionalityTests {

    // API code is hidden publically, but can be tested
    @Test
    public void testValidPubIds() {
        assertTrue(API.INSTANCE.isValidPubId(405));
        assertFalse(API.INSTANCE.isValidPubId(-1));
        assertFalse(API.INSTANCE.isValidPubId(9000));
    }
}