package ru.mipt.bit.platformer.util;

public interface WorldListener {
    void onBulletAdded(BulletModel bullet);
    void onBulletRemoved(BulletModel bullet);
    void onTankRemoved(TankModel tank);
}
