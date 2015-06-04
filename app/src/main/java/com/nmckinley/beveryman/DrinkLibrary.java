package com.nmckinley.beveryman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//package com.nmckinley.beveryman;

/**
 * Created by Zella on 6/3/2015.
 */
public class DrinkLibrary {

    /** Describes which solenoid is associated with each liquid */
    public enum Liquid {
        VODKA(0),
        TEQUILA(1),
        WHISKEY(2),
        RUM(3),
        TRIPLE_SEC(4),
        SOURS_MIX(5),
        ORANGE_JUICE(6),
        COKE(7);

        private int value;

        Liquid(int val) {
            this.value = val;
        }
    }

    /** Describes the drink order as per strings.xml - Note this is one-indexed! */
    public static final Map<Integer, Drink> drinkOrder;
    static {
        drinkOrder = new HashMap<Integer, Drink>();
        drinkOrder.put(1, Drink.WHISKEY_SOUR);
        drinkOrder.put(2, Drink.RUM_AND_COKE);
        drinkOrder.put(3, Drink.LONG_ISLAND_ICED_TEA);
        drinkOrder.put(4, Drink.BORDER_CROSSING);
        drinkOrder.put(5, Drink.BRASS_MONKEY);
        drinkOrder.put(6, Drink.CUBA_LIBRE);
        drinkOrder.put(7, Drink.DAIQUIRI);
        drinkOrder.put(8, Drink.EL_DORADO);
        drinkOrder.put(9, Drink.KAMIKAZE);
        drinkOrder.put(10, Drink.LEMON_DROP);
        drinkOrder.put(11, Drink.SCREWDRIVER);
        drinkOrder.put(12, Drink.TIGER_JUICE);
        drinkOrder.put(13, Drink.VIVA_VILLA);
        drinkOrder.put(14, Drink.AMBASSADOR);
        drinkOrder.put(15, Drink.BACARDI_COCKTAIL);
        drinkOrder.put(16, Drink.MAI_TAI);
        drinkOrder.put(17, Drink.MARGARITA);
        drinkOrder.put(18, Drink.NEW_YORKER);
        drinkOrder.put(19, Drink.TEQUILA_SUNRISE);
        drinkOrder.put(20, Drink.JACK_AND_COKE);
    }

    private class Amount {

        private Liquid drink;
        private int pourTime;

        public Amount(Liquid drink, int time) {
            this.drink = drink;
            this.pourTime = time;
        }
    }

    /** Describes the drink ingredients */
    public enum Drink {
        WHISKEY_SOUR(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.WHISKEY, 3),
                new Amount(Liquid.SOURS_MIX, 6)
        ))),
        RUM_AND_COKE(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.RUM, 3),
                new Amount(Liquid.COKE, 9)
        ))),
        LONG_ISLAND_ICED_TEA(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 2),
                new Amount(Liquid.VODKA, 2),
                new Amount(Liquid.RUM, 2),
                new Amount(Liquid.TRIPLE_SEC, 2),
                new Amount(Liquid.SOURS_MIX, 3),
                new Amount(Liquid.COKE, 1)
        ))),
        BORDER_CROSSING(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 2),
                new Amount(Liquid.SOURS_MIX, 1),
                new Amount(Liquid.COKE, 4)
        ))),
        BRASS_MONKEY(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.RUM, 2),
                new Amount(Liquid.VODKA, 2),
                new Amount(Liquid.ORANGE_JUICE, 4)
        ))),
        CUBA_LIBRE(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.RUM, 2),
                new Amount(Liquid.COKE, 5),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        DAIQUIRI(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.RUM, 4),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        EL_DORADO(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 3),
                new Amount(Liquid.SOURS_MIX, 2)
        ))),
        KAMIKAZE(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.VODKA, 3),
                new Amount(Liquid.TRIPLE_SEC, 2),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        LEMON_DROP(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.VODKA, 3),
                new Amount(Liquid.TRIPLE_SEC, 1),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        SCREWDRIVER(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.VODKA, 2),
                new Amount(Liquid.ORANGE_JUICE, 4)
        ))),
        TIGER_JUICE(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.WHISKEY, 3),
                new Amount(Liquid.ORANGE_JUICE, 2),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        VIVA_VILLA(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 2),
                new Amount(Liquid.SOURS_MIX, 3)
        ))),
        AMBASSADOR(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 2),
                new Amount(Liquid.ORANGE_JUICE, 3),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        BACARDI_COCKTAIL(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.RUM, 3),
                new Amount(Liquid.SOURS_MIX, 2)
        ))),
        MAI_TAI(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.RUM, 2),
                new Amount(Liquid.TRIPLE_SEC, 1),
                new Amount(Liquid.ORANGE_JUICE, 2),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        MARGARITA(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 2),
                new Amount(Liquid.TRIPLE_SEC, 1),
                new Amount(Liquid.SOURS_MIX, 1)
        ))),
        NEW_YORKER(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.WHISKEY, 3),
                new Amount(Liquid.SOURS_MIX, 2)
        ))),
        TEQUILA_SUNRISE(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.TEQUILA, 2),
                new Amount(Liquid.ORANGE_JUICE, 4)
        ))),
        JACK_AND_COKE(new HashSet<Amount>(Arrays.asList(
                new Amount(Liquid.WHISKEY, 3),
                new Amount(Liquid.COKE, 4)
        )));

        private int[] amounts = new int[] {0,0,0,0,0,0,0,0};

        Drink(Set<Amount> ingredients) {
            for (Amount a : ingredients) {
                amounts[a.drink.value] = a.pourTime;
            }
        }

        public String getInstructions() {
            String instructions = "";
            for (int i : amounts) {
                instructions = instructions + i + ",";
            }
            return instructions.substring(0, instructions.length()-2);
        }
    }

    protected static String getDrinkInstructions(int id) {
        Drink drink = drinkOrder.get(id);
        String drinkInstructions = drink.getInstructions();
        return drinkInstructions;
    }
}