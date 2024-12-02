package com.example.teachingapp.enums;

public enum PresenceType {
    O,
    N,
    S,
    U,
    NOT_GIVEN;

    public static Integer stringToPresenceType(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }

        switch (string.toUpperCase()) {
            case "O":
                return 1;
            case "N":
                return 2;
            case "S":
                return 3;
            case "U":
                return 4;
            default:
                return null;
        }
    }

    public static PresenceType stringPresenceType(String string) {
        if (string == null || string.isEmpty()) {
            return NOT_GIVEN;
        }

        switch (string.toUpperCase()) {
            case "O":
                return O;
            case "N":
                return N;
            case "S":
                return S;
            case "U":
                return U;
            default:
                return NOT_GIVEN;
        }
    }

    public static String shortToLongPresenceType(PresenceType presenceType) {
        switch (presenceType) {
            case O:
                return "Obecność";
            case N:
                return "Nieobecność";
            case S:
                return "Spóźnienie";
            case U:
                return "Usprawiedliwienie";
            default:
                return "Brak danych";
        }
    }

}
