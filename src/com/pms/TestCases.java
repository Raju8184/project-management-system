package com.pms;

import com.pms.models.*;
import java.util.*;

public class TestCases {
    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("=== Starting Unit Tests Validation ===");

        // TEST 1: WebDevelopmentProject Billing
        total++;
        try {
            WebDevelopmentProject wp = new WebDevelopmentProject("P1", "Web", 1000.0, new Date());
            if (Double.compare(wp.calculate_billing(), 1000.0) == 0) {
                System.out.println("[PASS] WebDevelopmentProject billing calculated correctly.");
                passed++;
            } else {
                System.out.println("[FAIL] WebDevelopmentProject billing mismatch. Expected: 1000.0, Got: "
                        + wp.calculate_billing());
            }
        } catch (Exception e) {
            System.out.println("[FAIL] WebDevelopmentProject threw exception: " + e.getMessage());
        }

        // TEST 2: MobileAppProject Billing
        total++;
        try {
            MobileAppProject mp = new MobileAppProject("P2", "Mobile", 1000.0, new Date(), Arrays.asList("iOS"));
            // Formula is 1.1x -> 1100.0
            if (Double.compare(mp.calculate_billing(), 1100.0) == 0) {
                System.out.println("[PASS] MobileAppProject billing calculated correctly.");
                passed++;
            } else {
                System.out.println(
                        "[FAIL] MobileAppProject billing mismatch. Expected: 1100.0, Got: " + mp.calculate_billing());
            }
        } catch (Exception e) {
            System.out.println("[FAIL] MobileAppProject threw exception: " + e.getMessage());
        }

        // TEST 3: DataScienceProject Billing
        total++;
        try {
            DataScienceProject dp = new DataScienceProject("P3", "DS", 1000.0, new Date(), 100.0f);
            // Formula is budget + (gb * 5.0) => 1000 + 500 = 1500.0
            if (Double.compare(dp.calculate_billing(), 1500.0) == 0) {
                System.out.println("[PASS] DataScienceProject billing calculated correctly.");
                passed++;
            } else {
                System.out.println(
                        "[FAIL] DataScienceProject billing mismatch. Expected: 1500.0, Got: " + dp.calculate_billing());
            }
        } catch (Exception e) {
            System.out.println("[FAIL] DataScienceProject threw exception: " + e.getMessage());
        }

        // TEST 4: Stage Advancements
        total++;
        try {
            DevelopmentTracker tracker = new DevelopmentTracker();
            if (tracker.current_stage() == ProjectStage.PLANNING) {
                tracker.advance_stage();
                if (tracker.current_stage() == ProjectStage.DEVELOPMENT) {
                    System.out.println("[PASS] DevelopmentTracker successfully steps from PLANNING to DEVELOPMENT.");
                    passed++;
                } else {
                    System.out.println("[FAIL] DevelopmentTracker stepped to wrong stage.");
                }
            } else {
                System.out.println("[FAIL] DevelopmentTracker didn't start at PLANNING.");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Tracker test threw exception: " + e.getMessage());
        }

        // TEST 5: Billing System Payments
        total++;
        try {
            Billing billing = new Billing("B", 100.0);
            billing.record_payment(50.0);
            if (billing.getStatus() == PaymentStatus.PARTIAL && billing.balance_due() == 50.0) {
                billing.record_payment(50.0);
                if (billing.getStatus() == PaymentStatus.PAID && billing.balance_due() == 0.0) {
                    System.out.println("[PASS] Billing accurately transitions between PARTIAL and PAID state.");
                    passed++;
                } else {
                    System.out.println("[FAIL] Billing did not transition to PAID.");
                }
            } else {
                System.out.println("[FAIL] Billing did not transition to PARTIAL or balance incorrect.");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Billing test threw exception: " + e.getMessage());
        }

        System.out.println("\n=== Test Results: " + passed + "/" + total + " Passed ===");
    }
}
