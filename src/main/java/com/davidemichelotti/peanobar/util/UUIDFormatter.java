/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.util;

import java.util.UUID;

/**
 *
 * @author david
 */
public class UUIDFormatter {
    public static UUID format(String rawWuid){
        UUID uuid;
        if (rawWuid.split("-").length != 5) {
            uuid = UUID.fromString(rawWuid.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));
        } else {
            uuid = UUID.fromString(rawWuid);
        }
        return uuid;
    }
}
