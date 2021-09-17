if (op.hasInsert()) {
        try{
        //do transaction insert operation
        }catch(RuntimeException err )

//check if the error is from a transaction operation failure
        if (err instanceof com.couchbase.transactions.error.external.TransactionOperationFailed) {
        com.couchbase.transactions.error.external.TransactionOperationFailed e =
        (com.couchbase.transactions.error.external.TransactionOperationFailed) err;

        //convert the error into a comparable form
        com.couchbase.grpc.protocol.ErrorWrapper.Builder ew = com.couchbase.grpc.protocol.ErrorWrapper.newBuilder()
        .setErrorClass(ResultsUtil.mapErrorClass(e.causingErrorClass()))
        .setAutoRollbackAttempt(e.autoRollbackAttempt())
        .setRetryTransaction(e.retryTransaction())
        .setCause(ExpectedCause.newBuilder().setException(ResultsUtil.mapCause(e.getCause())))
        .setToRaise(ResultsUtil.mapToRaise(e.toRaise()));

        boolean ok = false;

        // validate the error
        for (ExpectedResult er : expectedResults) {
        if (er.hasError()) {
        ErrorWrapper m = er.getError();
        if (m.getErrorClass() == ew.getErrorClass()
        && m.getAutoRollbackAttempt() == ew.getAutoRollbackAttempt()
        && m.getRetryTransaction() == ew.getRetryTransaction()
        && m.getToRaise() == ew.getToRaise()) {
        ExpectedCause c = m.getCause();

        if (c.getDoNotCheck() || c.equals(ew.getCause())) {
        ok = true; //set to true if we got the expected error
        }
        }
        }
        }

        if (!ok) {
        throw error;
        }
        else {
        boolean ok = false;
        // Get all the expected results
        for (ExpectedResult er : expectedResults) {
        ExternalException ee = ResultsUtil.mapCause(err);
        ExternalException expected = er.getException();
        if (expected != ExternalException.Unknown && ee.equals(expected)) {
        logger.info("Operation '{}' failed with {} as expected", opDebug, ee);
        ok = true;
        break;
        }
        }

        if (!ok) {
        // A stage can only raise an TransactionOperationFailed
        // Update: As of EXT_QUERY, that is no longer the case.
        String msg = String.format("Command %s raised error '%s', but require TransactionOperationFailed",
        opDebug, err);
        logger.warn(msg);
        dump(ctx);
        RuntimeException error = new InternalPerformerFailure(
        new IllegalStateException(msg));
        // Make absolutely certain the test fails
        testFailure.set(error);
        throw error;
        }
        }

        });
        }
