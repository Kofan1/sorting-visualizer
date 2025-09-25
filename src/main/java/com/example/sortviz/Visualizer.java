package com.example.sortviz;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Visualizer {
    private final Canvas canvas;
    private int[] data;
    private long delayMs = 30; // 默认速度
    private final List<Operation> ops = new ArrayList<>();
    private volatile int highlightA = -1, highlightB = -1;

    private Thread playerThread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(true);
    private int opIndex = 0;

    public Visualizer(Canvas canvas, int[] data) {
        this.canvas = canvas;
        this.data = data;
    }

    public void setData(int[] data) {
        this.data = data;
        this.highlightA = this.highlightB = -1;
        this.opIndex = 0;
    }

    public void setDelayMs(long ms) {
        this.delayMs = Math.max(0, ms);
    }

    public boolean isRunning() {
        return running.get();
    }

    public void clearOperations() {
        ops.clear();
        opIndex = 0;
    }

    public void loadOperations(List<Operation> operations) {
        clearOperations();
        ops.addAll(operations);
        draw();
    }

    public void play() {
        if (ops.isEmpty()) return;

        if (playerThread == null || !playerThread.isAlive()) {
            running.set(true);
            paused.set(false);
            playerThread = new Thread(this::runLoop, "PlayerThread");
            playerThread.setDaemon(true);
            playerThread.start();
        } else {
            paused.set(false);
        }
    }

    public void pause() {
        paused.set(true);
    }

    public void stop() {
        running.set(false);
        paused.set(true);
        if (playerThread != null) {
            try {
                playerThread.join(200);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void step() {
        if (ops.isEmpty() || opIndex >= ops.size()) return;
        apply(ops.get(opIndex++));
        draw();
    }

    private void runLoop() {
        while (running.get() && opIndex < ops.size()) {
            if (paused.get()) {
                sleep(10);
                continue;
            }
            apply(ops.get(opIndex++));
            Platform.runLater(this::draw);
            sleep(delayMs);
        }
        running.set(false);
        paused.set(true);
    }

    private void apply(Operation op) {
        switch (op.type()) {
            case SWAP -> {
                int i = op.i(), j = op.j();
                int tmp = data[i];
                data[i] = data[j];
                data[j] = tmp;
                highlightA = i;
                highlightB = j;
            }
            case SET -> {
                data[op.i()] = op.value();
                highlightA = op.i();
                highlightB = -1;
            }
            case HILITE -> {
                highlightA = op.i();
                highlightB = op.j();
            }
            case CLEAR_HILITE -> {
                highlightA = highlightB = -1;
            }
        }
    }

    public void draw() {
        final double w = canvas.getWidth();
        final double h = canvas.getHeight();

        int n = data.length;
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, w, h);

        if (n == 0) return;
        int max = Utils.max(data);

        double barW = Math.max(1, w / n);
        for (int i = 0; i < n; i++) {
            double x = i * barW;
            double barH = (h - 4) * (data[i] / (double) max);
            double y = h - barH;

            // 高亮当前操作的元素
            if (i == highlightA || i == highlightB) {
                g.setGlobalAlpha(0.9);
            } else {
                g.setGlobalAlpha(0.6);
            }
            g.fillRect(x, y, barW - 1, barH);
        }
        g.setGlobalAlpha(1.0);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
