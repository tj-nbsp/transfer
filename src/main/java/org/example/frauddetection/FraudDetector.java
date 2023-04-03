package org.example.frauddetection;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

public class FraudDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

    private static final long serialVersionUID = 1L;

    private static final double SMALL_AMOUNT = 1.00D;
    private static final double LARGE_AMOUNT = 500.00D;
    private static final long ONE_MINUTE = 60 * 1000;

    @Override
    public void processElement(Transaction transaction, Context ctx, Collector<Alert> out) throws Exception {
        Alert alert = new Alert();
        alert.setId(transaction.getAccountId());

        out.collect(alert);
    }

}
