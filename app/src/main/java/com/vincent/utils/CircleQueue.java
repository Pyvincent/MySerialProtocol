package com.vincent.utils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;


public class CircleQueue {
	private static final String TAG = CircleQueue.class.getSimpleName();
	private Queue<Byte> queue = new LinkedList<Byte>();
	private Object lock = new Object();
	
	public boolean read(int readSize, byte[] readBuffer) {
		if(queue.size() < readSize) {
			return false;
		}
		if(readBuffer == null || readBuffer.length < readSize) {
			Log.i(TAG, "readBuffer is null or length < readSize");
			return false;
		}
		
		synchronized(lock) {
			for(int i = 0; i < readSize; i++) {
				readBuffer[i] = queue.remove();
			}
		}
		
		return true;
	}
	
	public boolean prefetch(int readSize, byte[] readBuffer) {
		if(queue.size() < readSize) {
			return false;
		}
		if(readBuffer == null || readBuffer.length < readSize) {
			Log.i(TAG, "readBuffer is null or length < readSize");
			return false;
		}
		
		synchronized(lock) {
			Iterator<Byte> iterator = queue.iterator();
			int i = 0;
			while(iterator.hasNext()) {
				readBuffer[i] = (Byte) iterator.next();
				if(i ++ >= readSize) {
					break;
				}
			}
		}
		
		return true;
	}
	
	public boolean write(byte[] writeBuffer, int length) {
		if(writeBuffer == null || writeBuffer.length < length) {
			return false;
		}
		
		synchronized(lock) {
			for(int i = 0; i < length; i++) {
				queue.offer(writeBuffer[i]);
			}
		} 
		
		return true;
	}
	
	public int getSize() {
		synchronized(lock) {
			return queue.size();
		}
	}
}
