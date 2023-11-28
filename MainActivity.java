package com.example.exercise6;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
public static Boolean running = true;
public static Object lock1 = new Object();
public static Object lock2 = new Object();
public static Object lock3 = new Object();
public int count = 0;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
TextView change_color = findViewById(R.id.change_color);
TextView moving_banner = findViewById(R.id.moving_banner);
TextView counter = findViewById(R.id.counter);
Runnable runnable1 = new Runnable() {
@Override
public void run() {
while (true) {
synchronized (lock1) {
if (!running) {
try {
lock1.wait();
} catch (InterruptedException e) {
e.printStackTrace();
}
}
runOnUiThread(new Runnable() {
@Override
public void run() {
change_color.setTextColor(Color.parseColor("#00FF00"));
}
});
try {
Thread.sleep(500);
} catch (InterruptedException e) {
e.printStackTrace();
}
runOnUiThread(new Runnable() {
@Override
public void run() {
change_color.setTextColor(Color.parseColor("#FFFF00"));
}
});
try {
Thread.sleep(500);
} catch (InterruptedException e) {
e.printStackTrace();
}
runOnUiThread(new Runnable() {
@Override
public void run() {
change_color.setTextColor(Color.parseColor("#FF0000"));
}
});
try {
Thread.sleep(500);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}
}
};
Thread thread1 = new Thread(runnable1);
Runnable runnable2 = new Runnable() {
@Override
public void run() {
while (true) {
synchronized (lock2) {
if (!running) {
try {
lock2.wait();
} catch (InterruptedException e) {
e.printStackTrace();
}
}
runOnUiThread(new Runnable() {
@Override
public void run() {
ObjectAnimator left_to_right =
ObjectAnimator.ofFloat(moving_banner, "translationX", -200f, 200f);
left_to_right.setDuration(2000);
left_to_right.start();
}
});
try {
Thread.sleep(500);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}
}
};
Thread thread2 = new Thread(runnable2);
Runnable runnable3 = new Runnable() {
@Override
public void run() {
while (true) {
synchronized (lock3) {
if (!running) {
try {
lock3.wait();
} catch (InterruptedException e) {
e.printStackTrace();
}
}
runOnUiThread(new Runnable() {
@Override
public void run() {
counter.setText("Counter: " + count);
count++;
if(count > 1000) count = 0;
}
});
try {
Thread.sleep(500);
} catch (InterruptedException e) {
e.printStackTrace();
}
}
}
}
};
Thread thread3 = new Thread(runnable3);
Button start = findViewById(R.id.start);
start.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
thread1.start();
thread2.start();
thread3.start();
}
});
Button resume = findViewById(R.id.resume);
resume.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
running = true;
synchronized (lock1) {
lock1.notify();
}
synchronized (lock2) {
lock2.notify();
}
synchronized (lock3) {
lock3.notify();
}
}
});
Button stop = findViewById(R.id.stop);
stop.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
running = false;
}
});
}
}
