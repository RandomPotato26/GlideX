/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

/**
 * creates a timer class (basically)
 */
public final class ElapsedTime {
    private long time = 0;

	public ElapsedTime() {
		reset();
	}

	public void reset() {
		time = System.nanoTime();
	}

	public long getElapsedNano() {
		final long currTime = System.nanoTime();
		return currTime - time;
	}

	public long getElapsedMillis() {
		return getElapsedNano() / 1000000;
	}

	public double getElapsedSeconds() {
		return getElapsedNano() / 1000000000.0;
	}
}
