import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterTest {
    RPGCharacter myChar;
    RPGCharacter teamMateChar;
    RPGCharacter enemyChar;
    Thing thing;
    int distance = 0;


    @Before
    public void setUp() throws Exception {
        myChar = new RPGCharacter();
        teamMateChar = new RPGCharacter();
        enemyChar = new RPGCharacter();
        thing = new Thing();
    }

    @Test
    public void fullHealthOnCharacterCreation() {
        assertEquals(1000, myChar.health);
    }

    @Test
    public void startLevelOne() {
        assertEquals(1, myChar.level);
    }

    @Test
    public void aliveToStart() {
        assertTrue(myChar.isAlive());
    }

    @Test
    public void takeMoreDamageThanHealth_CharDies() {
        myChar.takeDamage(2000);
        assertFalse(myChar.isAlive());
    }

    @Test
    public void takeLessDamageThanHealth_CharLives() {
        myChar.takeDamage(100);
        assertTrue(myChar.isAlive());
    }

    @Test
    public void whenTakeDamage_HealthSubtracted() {
        myChar.takeDamage(250);
        assertEquals(750, myChar.health);
    }

    @Test
    public void takeSeveralHits_EventuallyDies() {
        assertEquals(700, myChar.takeDamage(300));
        assertEquals(400, myChar.takeDamage(300));
        assertEquals(100, myChar.takeDamage(300));
        myChar.takeDamage(100);
        assertFalse(myChar.isAlive());
    }

    @Test
    public void charactersCanDealDamageToOtherCharacters() {
        myChar.attack(enemyChar, distance);
        assertEquals(700, enemyChar.health);
    }

    @Test
    public void characterCannotAttackSelf() {
        myChar.attack(myChar, distance);
        assertEquals(1000, myChar.health);
    }

    @Test
    public void characterCannotAttackAlly() {
        myChar.joinFaction("rebels");
        teamMateChar.joinFaction("rebels");
        myChar.attack(teamMateChar, distance);
        assertEquals(1000, teamMateChar.health);
    }

    @Test
    public void characterCanAttackThings() {
        myChar.attack(thing);
        assertEquals(700, thing.health);
    }

    @Test
    public void whenThingHasHealth_thingNotDestroyed() {
        assertFalse(thing.isDestroyed());
    }

    @Test
    public void whenThingHasNoHealth_thingIsDestroyed() {
        thing.takeDamage(1000);
        assertTrue(thing.isDestroyed());
    }

    @Test
    public void whenTargetIs5OrMoreLevelsAbove_DamageReduced() {
        enemyChar.level = 6;
        myChar.attack(enemyChar, distance);
        assertEquals(850, enemyChar.health);
    }

    @Test
    public void whenTargetIs5OrMoreLevelsBelow_DamageIncreased() {
        myChar.level = 6;
        myChar.attack(enemyChar, distance);
        assertEquals(550, enemyChar.health);
    }

    @Test
    public void deadCharactersCannotBeHealed() {
        myChar.takeDamage(999999999);
        myChar.heal(teamMateChar);
        assertFalse(myChar.isAlive());
        assertEquals(0, myChar.health);
    }

    @Test
    public void charactersCannotHealEnemies() {
        enemyChar.takeDamage(300);
        myChar.heal(enemyChar);
        assertEquals(700, enemyChar.health);
    }

    @Test
    public void charactersCanHealAllies() {
        myChar.factions.add("rebels");
        teamMateChar.factions.add("rebels");
        teamMateChar.takeDamage(300);
        myChar.heal(teamMateChar);
        assertEquals(900, teamMateChar.health);
    }

    @Test
    public void characterCanHealSelf() {
        myChar.takeDamage(300);
        myChar.heal(myChar);
        assertEquals(900, myChar.health);
    }

    @Test
    public void healCannotExceed1000() {
        myChar.heal(teamMateChar);
        assertEquals(1000, teamMateChar.health);
    }

    @Test
    public void charactersHaveMaxRange() {
        assertNotNull(myChar.maxRange);
    }

    @Test
    public void meleeFightersHaveRangeOf2() {
        RPGCharacter meleeFighter = new MeleeFighter();
        assertEquals(2, meleeFighter.maxRange);
    }

    @Test
    public void rangedFightersHaveRangeOf20() {
        RPGCharacter rangedFighter = new RangedFighter();
        assertEquals(20, rangedFighter.maxRange);
    }

    @Test
    public void characterOutOfRange_noDamageDealt() {
        distance = 200;
        myChar.attack(enemyChar, distance);
        assertEquals(1000, enemyChar.health);
    }

    @Test
    public void newChar_BelongsToNoFaction() {
        assertEquals(0, myChar.factions.size());
    }

    @Test(expected = RPGCharacter.NoMoreFactionException.class)
    public void leaveWithNoFaction_ThrowsNoMoreFactionException() {
        myChar.leaveFaction("test");
    }

    @Test
    public void joinFaction() {
        myChar.joinFaction("test");
        assertEquals(1, myChar.factions.size());
    }

    @Test
    public void joinFaction_LeaveFaction() {
        myChar.joinFaction("test");
        myChar.leaveFaction("test");
        assertEquals(0, myChar.factions.size());
    }

    @Test
    public void leaveFactionSpecified() {
        myChar.joinFaction("loyal");
        myChar.joinFaction("betray");
        myChar.leaveFaction("betray");
        assertEquals("loyal", myChar.factions.get(0));
    }

    @Test
    public void sameFaction_isAlly() {
        myChar.joinFaction("rebels");
        teamMateChar.joinFaction("rebels");
        assertTrue(myChar.isAlly(teamMateChar));
    }

    @Test
    public void noFactionInCommon_notAlly() {
        myChar.joinFaction("rebels");
        enemyChar.joinFaction("imperials");
        assertFalse(myChar.isAlly(enemyChar));
    }

    // https://github.com/ardalis/kata-catalog/blob/master/katas/RPG%20Combat.md
}
