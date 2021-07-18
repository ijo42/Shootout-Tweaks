package ru.ijo42.shootout;

public class Config {

    public Modules modules;
    public BulletDamage bulletDamage;

    public static class Modules {
        public boolean bulletDamage;
    }

    public static class BulletDamage {
        public String takeDamageColor;
        public String giveDamageColor;
        public int delay;
    }
}

/*
*   "modules": [
    "bulletDamage": true
  ],
  "bulletDamage": [
    "takeDamageColor": "#328EFF",
    "giveDamageColor": "#FF1E1E",
	"delay": 3
  ]
* */