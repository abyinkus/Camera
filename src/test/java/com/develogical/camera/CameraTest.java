package com.develogical.camera;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CameraTest {



    private static Sensor sensor;
    private static MemoryCard memoryCard;

    @Before
    public  void setUp () {
        sensor = Mockito.mock(Sensor.class);
        memoryCard = Mockito.mock(MemoryCard.class);
    }

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

        final Camera camera = new Camera (sensor, memoryCard);
        camera.powerOn();
        verify(sensor, times(1)).powerUp();

    }


    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {

        final Camera camera = new Camera (sensor, memoryCard);
        camera.powerOff();
        verify(sensor, times(1)).powerDown();

    }

    @Test
    public void pressingTheShutterWhenPowerIsOffDoesNothing() {

        final Camera camera = new Camera (sensor, memoryCard);

        camera.pressShutter ();

        verify(sensor, times(0)).powerDown();
        verify(sensor, times(0)).powerUp();
        verify(sensor, times(0)).readData();

    }

    @Test
    public void pressingTheShutterWhenPowerIsOnCopyData() {

        final Camera camera = new Camera (sensor, memoryCard);

        byte [] bytes = {12,11,10};

        when(sensor.readData()).thenReturn(bytes);

        camera.powerOn();
        camera.pressShutter();

        verify(sensor, times(0)).powerDown();
        verify(sensor, times(1)).powerUp();
        verify(memoryCard, times(1)).write(eq(bytes), any());

    }


    @Test
    public void switchingCameraOffDoesNotWorkWhenWritingData() {

        final Camera camera = new Camera (sensor, memoryCard);

        byte [] bytes = {12,11,10};

        when(sensor.readData()).thenReturn(bytes);

        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();

        verify(sensor, times(0)).powerDown();
        verify(sensor, times(1)).powerUp();
        verify(memoryCard, times(1)).write(eq(bytes), any());

    }

    @Test
    public void switchingCameraOffWorksWhenWritingDataHasCompleted() {

        final Camera camera = new Camera (sensor, memoryCard);

        byte [] bytes = {12,11,10};

        when(sensor.readData()).thenReturn(bytes);

        camera.powerOn();
        camera.pressShutter();
        camera.writeComplete();
        camera.powerOff();

        verify(sensor, times(1)).powerDown();
        verify(sensor, times(1)).powerUp();
        verify(memoryCard, times(1)).write(eq(bytes), any());

    }

}

