package io.github.onran0.retrogltf.loader;

import java.util.*;

public class Profiler {
    private static final boolean ACTIVE = Boolean.getBoolean("retrogltf.enableProfiler");

    private static boolean doTrack = true;

    private static final Map<LoaderTaskType, Long> executionTimeMap = new EnumMap<>(LoaderTaskType.class);

    private static LoaderTaskType currentTask;
    private static long taskTrackAt;

    private static final ArrayDeque<LoaderTaskType> tasksStack = new ArrayDeque<>();
    private static final ArrayDeque<Long> tasksTrackTimeStack = new ArrayDeque<>();

    public static boolean isActive() {
        return ACTIVE;
    }

    public static Optional<Long> getTaskNanoExecutionTime(LoaderTaskType taskType) {
        return Optional.ofNullable(executionTimeMap.get(taskType));
    }

    public static Optional<Long> getTaskMsExecutionTime(LoaderTaskType taskType) {
        Long nano = executionTimeMap.get(taskType);

        if(nano == null) {
            return Optional.empty();
        } else {
            return Optional.of(nano / 1_000_000L);
        }
    }

    public static Set<LoaderTaskType> getTrackedTasksList() {
        return executionTimeMap.keySet();
    }

    public static Map<LoaderTaskType, Long> getTasksNanoExecutionTimeMap() {
        return Collections.unmodifiableMap(executionTimeMap);
    }

    public static Map<LoaderTaskType, Long> getTasksMsExecutionTimeMap() {
        Map<LoaderTaskType, Long> msExecutionTimeMap = new HashMap<>();

        for(LoaderTaskType taskType : executionTimeMap.keySet()) {
            long nano = executionTimeMap.get(taskType);

            msExecutionTimeMap.put(taskType, nano / 1_000_000L);
        }

        return Collections.unmodifiableMap(msExecutionTimeMap);
    }

    public static long summaryNs() {
        long ns = 0;

        for(LoaderTaskType taskType : executionTimeMap.keySet()) {
            ns += executionTimeMap.get(taskType);
        }

        return ns;
    }

    public static long summaryMs() {
        return summaryNs() / 1_000_000L;
    }

    public static void printNano() {
        printResults("ns", 1);
    }

    public static void printMillis() {
        printResults("ms", 1_000_000L);
    }

    private static void printResults(String timeUnit, long timeDivider) {
        int maxTaskLiteralLength = 0;
        int maxExecTimeLength = 0;

        long nanoSummary = summaryNs();

        List<Map.Entry<LoaderTaskType, Long>> trackedTasksList = new ArrayList<>(executionTimeMap.entrySet());

        trackedTasksList.sort(
                (a, b) -> (int) (b.getValue() - a.getValue())
        );

        for(Map.Entry<LoaderTaskType, Long> entry : trackedTasksList) {
            int taskLiteralLength = entry.getKey().getLiteral().length();
            int execTimeLength = divideAndStringifyNanoTime(entry.getValue(), timeDivider).length();

            if(taskLiteralLength > maxTaskLiteralLength) {
                maxTaskLiteralLength = taskLiteralLength;
            }

            if(execTimeLength > maxExecTimeLength) {
                maxExecTimeLength = execTimeLength;
            }
        }

        int i = 0;

        for(Map.Entry<LoaderTaskType, Long> task : trackedTasksList) {
            LoaderTaskType taskType = task.getKey();
            long nanoTime = task.getValue();

            String numStr = divideAndStringifyNanoTime(nanoTime, timeDivider);

            System.out.printf(
                    Locale.US,
                    "%d) %s%s= %s%s%s(%.3f%%)\n",
                    ++i,
                    taskType.getLiteral(),
                    repeatSpace(maxTaskLiteralLength - taskType.getLiteral().length() + 1),
                    numStr, timeUnit,
                    repeatSpace(maxExecTimeLength - numStr.length() + timeUnit.length() + 1),
                    (double) nanoTime / nanoSummary * 100.0D
            );
        }

        System.out.printf(
                "----------------------------------\nsummary = %s%s\n",
                divideAndStringifyNanoTime(nanoSummary, timeDivider), timeUnit
        );
    }

    public static void clear() {
        executionTimeMap.clear();
        tasksStack.clear();
        tasksTrackTimeStack.clear();
    }

    public static void setEnabledTrack(boolean enabled) {
        doTrack = enabled;
    }

    public static boolean isTrackEnabled() {
        return doTrack;
    }

    public static void startTaskTrack(LoaderTaskType task) {
        if(ACTIVE && doTrack) {
            currentTask = task;
            taskTrackAt = System.nanoTime();

            tasksStack.push(currentTask);
            tasksTrackTimeStack.push(taskTrackAt);
        }
    }

    public static void endTaskTrack() {
        if(ACTIVE && doTrack) {
            long endTime = System.nanoTime();

            long oldTime = executionTimeMap.containsKey(currentTask) ? executionTimeMap.get(currentTask) : 0;

            long duration = endTime - taskTrackAt;

            executionTimeMap.put(currentTask, duration + oldTime);

            tasksStack.pop();
            tasksTrackTimeStack.pop();

            if(tasksStack.isEmpty()) {
                currentTask = null;
                taskTrackAt = -1;
            } else {
                currentTask = tasksStack.peek();
                taskTrackAt = tasksTrackTimeStack.pop() + duration;
                tasksTrackTimeStack.push(taskTrackAt);
            }
        }
    }

    private static String repeatSpace(int times) {
        StringBuilder res = new StringBuilder();

        for(int i = 0; i < times; i++) {
            res.append(" ");
        }

        return res.toString();
    }

    private static String divideAndStringifyNanoTime(long nanoTime, long divider) {
        if(divider == 1L) {
            return String.valueOf(nanoTime);
        } else {
            return String.format(Locale.US, "%.3f", nanoTime / (double) divider);
        }
    }
}