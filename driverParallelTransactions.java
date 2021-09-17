@Test
    void twoConcurrentTransactionsInsertingSameDoc() {
            String docId = TestUtils.docId(collection, 0);
            String aInsertedDoc = "A has inserted doc";
            String bTriesToInsertDoc = "B tries to insert doc";
            String aHasCommitted = "A has committed";
            TransactionBuilder a =
            TransactionBuilder.create(shared, PerformerA,  "A")
            .insert(docId, initial)
            .setLatch(aInsertedDoc)  //Txn A stages an insert and signals TXN B to insert
            .waitForLatch(bTriesToInsertDoc) // waits until B inserts same doc
            .commit()
            .setLatch(aHasCommitted);

            TransactionBuilder b =
            TransactionBuilder.create(shared,PerformerA, "B")
            .addAttempt(attempt()
            .waitForLatch(aInsertedDoc) //Txn B waits until A inserts the doc

// Since A has staged insert then B tries to insert, it should fail with write-write conflict
            .insert(collection, docId, initial, EXPECT_FAIL_WRITE_WRITE_CONFLICT))

// Since the insert has failed, B tried to start an attempt to insert
            .addAttempt(attempt()
            .setLatch(bTriesToInsertDoc)
            .waitForLatch(aHasCommitted) // waits until A commits its insert
// This time, A has committed so doc already exists. Thus B fails with doc exists exception
            .insert(collection, docId, initial, EXPECT_FAIL_DOC_ALREADY_EXISTS));

// The ConcurrentTransactionCoordinator does the hard work of running both transactions, and handling the bi-directional stream to them both.
            List<TransactionResult> results = ConcurrentTransactionCoordinator.create(Arrays.asList(a, b))
        .addLatch(aInsertedDoc, 1)
        .addLatch(bTriesToInsertDoc, 1)
        .addLatch(aHasCommitted, 1)
        .blockOnResults();

//Perform result validation
