package com.develogical.camera;

public class Camera implements WriteCompleteListener {

    private Sensor sensor;
    private State state = State.OFF;
    private MemoryCard memoryCard;
    private boolean isWriteComplete = true;

    public Camera (final Sensor sensor, final MemoryCard memoryCard ) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        // not implemented
        if (state == State.ON) {

            byte [] bytes = sensor.readData();
            memoryCard.write(bytes, this);
            isWriteComplete = false;
        }
    }

    public void powerOn() {
        sensor.powerUp();
        state= State.ON;
    }

    public void powerOff() {

        if (isWriteComplete) {
            sensor.powerDown();
            state = State.OFF;
        }
    }


    @Override
    public void writeComplete() {
        isWriteComplete = true;
    }
}

