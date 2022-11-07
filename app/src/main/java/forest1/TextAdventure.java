package forest1;
import java.util.*;

public class TextAdventure {
  final FancyConsole console;
  final Scanner s;
  Player hero;
  boolean primaryEndCondition;

  public TextAdventure() {
    console = new FancyConsole("Great Text Adventure!", 600, 1000);
    s = new Scanner(System.in);
    primaryEndCondition = false;

    // feel free to change the player's starting values
    hero = new Player("", 100, 0);
  }

  /**
   * Ask a multiple choice question without a question prompt
   * @param choices choice list, add a string argument for each choice
   * @return integer (index 1) representing chosen response
   */
  public int listChoices(String... choices) {
    return multichoice(null, choices);
  }

  /**
   * Ask a "Do you:" question of the player
   * @param choices choice list, add a string argument for each choice
   * @return integer (index 1) representing chosen response
   */
  public int ask(String... choices) {
    return multichoice("Do you:", choices);
  }

  /**
   * Internal use by {@link #ask(String...)} and {@link #listChoices(String...)} only
   * 
   * Ask a multiple choice question of the player with a custom prompt
   * @param preceder question prompt
   * @param choices choice list, add a string argument for each choice
   * @return integer (index 1) representing chosen response
   */
  private int multichoice(String preceder, String... choices) {
    String toPrint = "\n" + (!preceder.isEmpty() ? preceder + "\n" : "");
    for (int i = 0; i < choices.length; i++) {
      String choice = choices[i];
      if (choice.isEmpty()) continue;
      toPrint += (i + 1) + " - " + choice + "\n";
    }
    toPrint += hero.getName() + ": ";
    System.out.println(toPrint);
    String res = s.nextLine();
    System.out.println("=================================================================");
    if (isParsable(res)) {
      return Integer.parseInt(res);
    } else {
      return 0;
    }
  }

  public void noop() {
    System.out.println("You became confused and did something else, you died.");
    gameEnd();
    return;
  }

  public void sleep() {
    console.setImage("graveyard.jpg");
    System.out.println("At some point in the night, a beast of the darkwood mauled you. Game over.");
    gameEnd();
    return;
  }

  public void play() {
    playa(false);
  }

  public void play(boolean continuation) {
    playa(continuation);
  }

  public void playa(boolean continuation) {
    // start of adventure. You can change this if you like
    console.setImage("distantcity.jpg");

    if (hero.getName().isEmpty()) {
      String input = "";
      System.out.println("What is your name?\n");
      input = s.nextLine();

      if (input.isEmpty()) {
        System.out.println("Name cannot be empty");
        play();
        return;
      }

      hero.changeName(input + " ");
    }
    System.out.println(
        (!continuation ? "You wake up to find yourself on the edge of a shadowy forest with the sun nearly set. \n"
            : "") + "You see what looks like a city in the distance. \nWhat would you like to do?");

    int c = ask("go towards the city", "turn around and re-enter the forest", "go back to sleep");
    /*
     * switch (c) { case "city": { enterZone1(); break; } case "forest": {
     * enterZone2(); break; } case "nap": { enterZone3(); break; } default: {
     * play(); break; }
     */
    if (c == 1) {
      enterZone1();
    } else if (c == 2) {
      enterZone2();
    } else if (c == 3) {
      sleep();
    } else {
      play();
    }
  }

  /**
   * Enter zone 1
   */
  private void enterZone1() {
    // change image
    console.setImage("distantcity.jpg");

    // describe the area/situation to the user.
    // Give them options for choices.
    System.out.println(
        "As you approach the city wall, a 3 foot bolt slams into the ground in front of you.\nYou hear a voice call down from above, \"leave this area or you will be killed.\"");

    // Take action or go to another zone based on their choice
    int r = ask("run", "do something stupid");
    if (!(r == 1)) {// i know this is convoluted, i needed a 'not'
      console.setImage("crossbones.jpg");
      System.out.println("You are killed.");
      gameEnd();
      return;
    }
    System.out.println(
        "You turn around and bolt. You hear \"Okay boys, target practice!\" as you take flight.\nBolts start hitting the ground around you.");
    int chance = (int) (Math.random() * 5);
    if (chance > 2) {
      System.out.println("One of the bolts hits you, and you fall. Tough luck.");
      gameEnd();
      return;
    }
    hero.changeName(hero.getName() + "🏅");
    play(true);
  }

  /**
   * Enter zone 2
   */
  private void enterZone2() {
    // change image
    console.setImage("forest.jpg");

    // describe the area/situation to the user.
    // Give them options for choices.
    System.out.println(
        """
        The world darkens as you enter. There is a dense fog below your waist. The spirit of Merasmus
        floats above you. As you walk, you can hear engines revving in the distance.
        """);

    // Take action or go to another zone based on their choice
    int r = ask("climb a tree", "go toward the noise", "sing", "sleep");
    if (r == 1) {// tree
      enterZone4();
      return;
    } else if (r == 2) {// go to engines
      enterZone5();
    } else if (r == 3) {// sing
      enterZone3();
      return;
    } else if (r == 4) {// sleep
      sleep();
      return;
    } else {
      noop();
      return;
    }
  }

  /**
   * Enter zone 3 (singing in forest)
   */
  private void enterZone3() {
    console.setImage("golden_forest.jpg");
    System.out.println(
        "You begin to sing. As your warm melody fills the forest, the trees\npart and the sunlight shines through the canopy. Birds begin circling your\nperformance, their song joining yours.\n\nAll of a sudden, you feel an impact behind you: One of the birds has doven at you.\nAnother one comes at your face, and you bat it away. Your singing becomes shaky\nand the chirping overwhelms your voice. One by one, they all begin coming at you.");
    int r = ask("run", "cower", "fight");
    if (r == 1) {// run
      System.out.println(
          "You try to launch yourself into a ravine on your right, but a root catches your foot. You must fight.");
      r = ask("continue");
      birdFight();
      return;
    } else if (r == 2) {// cower
      System.out.println("Your consitution fails you when it comes time for fight or flight. You lay flat and accept your fate.");
      r = ask("continue");
      System.out.println("The danger passes. Apparently the birds couldn't see cowards.");
      enterZone5();
      return;
    } else if (r == 3) {// fight
      System.out.println("For the first time in a while, you feel alive. Without hesitation, you decide to fight.");
      birdFight();
      return;
    }
    console.setImage("crossbones.jpg");
    System.out.println("You take too long to make a decision, and terror sets in. You pass out");
    gameEnd();
    return;
  }

  private void birdFight() {
    console.setImage("bird_formation.webp");
    int hp = hero.getHealth();
    int ehp = 40;
    while (hp > 0 && ehp > 0) {
      String hs = "";
      for (int i = 0; i < hp / 2; i++) hs += "+";
      String ehs = "";
      for (int i = 0; i < ehp / 2; i++) ehs += "+";
      System.out.println("HP: " + hs + "\nEnemy: " + ehs + "\nQuick, you must take action!");
      int die = (int) (Math.random() * 10);// enemy strategy quality
      int choice = ask("attack", "defend");
      if (choice == 1) {
        int att = (int) (Math.random() * 10);// hero attack quality
        System.out.println("Attack die: " + att + " Enemy die: " + die);
        if (att > 3 && die <= 7) {
          int damage = (int) (Math.random() * 6) + 5;
          ehp -= damage;
          if (ehp < 0) {
            ehp = 0;
          }
          System.out.println("Nice! Your random swings take out " + damage + " birds in one bout.\nThere are " + ehp + " left.");
        } else {
          if (die > 7) {
            hp -= 5;
            System.out.println("Miss. Your enemy was prepared. 5 damage.");
          } else {
            hp -= 1;
            System.out.println("Miss. Your wild swing contacts a tree. 1 damage.");
          }
        }
      } else if (choice == 2) {
        int def = (int) (Math.random() * 10);// hero defense quality
        System.out.println("Defense die: " + def + " Enemy die: " + die);
        if (def > 2) {
          if (die > 8) {
            System.out.println("Good thing you defended, your enemy was ready for you. No damage.");
          } else {
            System.out.println("You defend successfully.");
          }
        } else {
          int damage = die + 10;
          hp -= damage;
          System.out.println("You defend poorly, the enemy has you. " + damage + " damage.");
        }
      } else {
        hp = -1;
        break;
      }
    }
    if (hp < 0) {
      console.setImage("crossbones.jpg");
      System.out.println("Sucks to suck, but sometimes losing is the key to finding success. Except now you're dead.");
      gameEnd();
      return;
    }
    System.out.println("Way to go, champ! You won.");
    hero.changeName(hero.getName() + "🏆");
    hero.setHealth(hp);
    enterZone5();
    return;
  }

  /**
   * Tree
   */
  private void enterZone4() {
    // change image
    console.setImage("sun.jpg");

    System.out.println("""
      All of the trees have nice climbable bark, and you quickly make your way up to the high
      canopy. The sun is too bright and you can't see much. You head back down.""");

    int r = ask("try again, surely you will see something this time", "give up");
    if (r == 1) {
      int x = (int) (Math.random() * 50);
      if (x < 5) {// bird of prey
        System.out.println("""
          Above the canopy, you see a vast forest illuminated by the two suns over head.
          """);
        enterZone2();
      } else if (x < 25) {// bird
        System.out.println("Woah! You see a bird! You'll remember this moment. You head back down.");
        enterZone2();
      } else {// nothing
        enterZone4();
      }
      return;
    } else if (r == 2) {
      enterZone2();
      return;
    }
    noop();
  }

  /**
   * Engine noises
   */
  private void enterZone5() {//[ ] finish atv scene
    // change image
    //

    //
    System.out.println("""
      It's beautiful. You have found a large ravine, and inside are thousands of wild ATV's
      drinking from a stream of oil sludge. You can recall stories that your grandmother used to tell you of ATVs that would ride in and out of Valhalla, guiding warriors to their salvation.
      """);

    //
    int r = ask("feel a sudden urge to mount one?", "want to steal their gas?", "try to spook them?");
    if (r == 1) {// mount
      System.out.println("Are you sure?");
      int conf = listChoices("yes", "no");
      if (conf == 1) {
        System.out.println("You feel an impending sense of uncertainty. Are you really sure?");
        conf = listChoices("yes", "no");
        if (conf == 1) {
          System.out.println("""
            Wow, that's pretty sure. As you make your way to the herd, one looks up at you,
            flashing its blinkers. You approach it, and it revs its engine.
            """);
        } else {
          noop();
        }
      } else {
        noop();
      }
      return;
    } else if (r == 2) {// gas
      return;
    } else if (r == 3) {// spook
      return;
    }
    noop();
  }

  /**
   * Gates of valhalla
   */
  private void enterZone6() {//[ ] finish valhalla scene
    // change image
    //

    //

    //

  }

  /**
   * End the game
   */
  private void gameEnd() {//TODO add functionality for game completion check, when executed in #enterZone6()
    int r = ask("restart", "exit");
    if (r == 1) {
    hero = new Player("", 100, 10);
      play();
      return;
    }
    s.close();
    System.exit(0);
  }

  public static boolean isParsable(String input) {
    try {
        Integer.parseInt(input);
        return true;
    } catch (final NumberFormatException e) {
        return false;
    }
  }
}