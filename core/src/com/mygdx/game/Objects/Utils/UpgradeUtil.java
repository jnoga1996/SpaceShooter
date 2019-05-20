package com.mygdx.game.Objects.Utils;

import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Objects.Upgrade;
import com.mygdx.game.Objects.UpgradeType;

import java.util.Random;

public class UpgradeUtil {
    private Array<Upgrade> upgrades;
    private float worldWidth;
    private int upgradesCounter = 0;
    final static int UPGRADES_LIMIT = 2;

    public UpgradeUtil(Array<Upgrade> upgrades, float worldWidth) {
        this.upgrades = upgrades;
        this.worldWidth = worldWidth;
    }

    public void createNewUpgrade() {
        Random random = new Random();
        Upgrade upgrade = new Upgrade(UpgradeType.values()[random.nextInt(UpgradeType.values().length)]);
        upgrade.setPosition(worldWidth);
        upgrades.add(upgrade);
    }

    public void updateUpgrades(float delta) {
        for (Upgrade upgrade : upgrades) {
            upgrade.update(delta);
        }

        if (upgradesCounter < UPGRADES_LIMIT)
            checkIfNewUpgradeIsNeeded();
        removeOffScreenUpgrades();
    }

    public void removeOffScreenUpgrades() {
        if (!upgrades.isEmpty()) {
            Upgrade firstUpgrade = upgrades.peek();
            if (firstUpgrade.getX() < -Upgrade.COLLISION_CIRCLE_RADIUS) {
                upgrades.removeValue(firstUpgrade, true);
                upgradesCounter--;
            }
        }
    }

    public void checkIfNewUpgradeIsNeeded() {
        if (upgrades.isEmpty()) {
            createNewUpgrade();
        } else {
            Upgrade upgrade = upgrades.peek();
            if (upgrade.getX() < worldWidth) {
                createNewUpgrade();
            }
        }
        upgradesCounter++;
    }
}
