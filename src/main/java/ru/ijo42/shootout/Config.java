package ru.ijo42.shootout;

public class Config {

    public boolean debug = false;

    public boolean alterWorldGrab = false;

    public Modules modules = new Modules();

    public BulletDamage bulletDamage = new BulletDamage();

    public static class Modules {
        public boolean bulletDamage = true;
    }

    public static class BulletDamage {
        public String takeDamageColor = "#328EFF";
        public String giveDamageColor = "#FF1E1E";
        public int delay = 3;
        public String[] enableWorlds = {
                "DIM1",
                "DIM-1",
                "world"
        };
    }
}

/*
 {
  "debug": true,
  "alterWorldGrab": false,
  "modules": {
    "bulletDamage": true
  },
  "bulletDamage": {
    "takeDamageColor": "#328EFF",
    "giveDamageColor": "#FF1E1E",
    "delay": 3,
    "enableWorlds": [
       "DIM1",
       "DIM-1",
       "hub"
    ]
  }
}
*/