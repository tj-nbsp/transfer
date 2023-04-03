package org.example.frauddetection;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.walkthrough.common.entity.Transaction;

/**
 * status + time = ❤️
 * 出现了连续两次消费, 第一次小于 1, 第二次大于 500, 并且两次消费的时间隔在一分钟以内的账户需要打印警告信息.
 */
public class FraudDetector extends KeyedProcessFunction<Long, Transaction, Alert> {

    private static final long serialVersionUID = 1L;

    // action scope is limited to current key.
    private transient ValueState<Boolean> flagState;
    private transient ValueState<Long> timerState; // 只是为了清除定时器而使用了这个变量

    private static final double SMALL_AMOUNT = 1.00D;
    private static final double LARGE_AMOUNT = 500.00D;
    private static final long ONE_MINUTE = 60 * 1000;

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<Boolean> flagDescriptor = new ValueStateDescriptor<>(
                "flag",
                Types.BOOLEAN);
        flagState = getRuntimeContext().getState(flagDescriptor);

        ValueStateDescriptor<Long> timerDescriptor = new ValueStateDescriptor<Long>(
                "timer-state",
                Types.LONG);
        timerState = getRuntimeContext().getState(timerDescriptor);
    }

    @Override
    public void processElement(Transaction transaction, Context ctx, Collector<Alert> out) throws Exception {
        Boolean lastTransactionWasSmall = flagState.value();

        if (lastTransactionWasSmall != null) {
            if (transaction.getAmount() > LARGE_AMOUNT) {
                Alert alert = new Alert();
                alert.setId(transaction.getAccountId());
                out.collect(alert);
            }
            // 没有出现风险或者已经捕获到了风险，清除定时器和相关状态
            Long timer = timerState.value();
            ctx.timerService().deleteProcessingTimeTimer(timer);
            stateClear();
        }

        if (transaction.getAmount() < SMALL_AMOUNT) {
            flagState.update(true);

            // 出现了小额，添加定时器监控
            long timer = ctx.timerService().currentProcessingTime() + ONE_MINUTE;
            ctx.timerService().registerProcessingTimeTimer(timer);
            timerState.update(timer);
        }
    }

    @Override
    public void onTimer(long timestamp, KeyedProcessFunction<Long, Transaction, Alert>.OnTimerContext ctx, Collector<Alert> out) throws Exception {
        // 到时间了需要清除状态来恢复到初始情况
        stateClear();
    }

    private void stateClear() {
        timerState.clear();
        flagState.clear();
    }

}
