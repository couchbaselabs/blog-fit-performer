@IgnoreWhen(numberOfPerformersIsAbove = 1,
        firstPerformerMissesCapabilities = {PerformerCaps.EXT_CUSTOM_METADATA_COLLECTION},
        clusterMissesCapabilities = {Capabilities.COLLECTIONS})

@Test
    void expiryDuringFirstOpInTransactionEntersExpiryOvertime() {
            String docId = TestUtils.docId(collection, 0);

            TransactionResult result = TransactionBuilder.create(shared) //connects to performer snd retrieves all required information
            .injectExpiryAtPoint(StagePoints.HOOK_INSERT)
            .insert(docId, updated, EXPECT_FAIL_EXPIRY)
            .sendToPerformer();

            ResultValidator.assertNotStarted(collection, result);
            DocValidator.assertDocDoesNotExist(collection, docId);
            assertEquals(TransactionException.EXCEPTION_EXPIRED, result.getException());
            }


         //   Code Snippet from ExecuteWhen:
           // Note: “shared” is a Java Object which contains all the performer capabilities
// Check if numberOfPerformersIsAbove = 1
            if (found.numberOfPerformersIsAbove() != 0) {
            if (shared.numberOfPerformers() > found.numberOfPerformersIsAbove()) {
            return ConditionEvaluationResult.disabled("Test disabled because number of performers " +
            shared.numberOfPerformers() + " is above required " + found.numberOfPerformersIsAbove());
            }
            }

// check if performer supports feature EXT_CUSTOM_METADATA_COLLECTION
            for (PerformerCaps neededCapability : found.firstPerformerMissesCapabilities()) {
            if (!shared.firstPerformer().performerCaps().contains(neededCapability)) {
            return ConditionEvaluationResult.disabled("Test disabled because capability of " +
            neededCapability + " is missing from first performer based on @IgnoreWhen");
            }
            }


// check if the couchbase cluster supports a feature required for the transactions
            for (com.couchbase.transactions.capabilities.Capabilities neededCapability : found.clusterMissesCapabilities()) {
            if (!testCluster.config().capabilities().contains(neededCapability)) {
            return ConditionEvaluationResult.disabled("Test disabled because capability of " +
            neededCapability + " is missing on cluster based on @IgnoreWhen");
            }
