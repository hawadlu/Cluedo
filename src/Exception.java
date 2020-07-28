/**
 * Classes containing any potential errors
 */

class InvalidActionException extends Throwable {
    InvalidActionException(String specifics) {
        System.out.println("Move is invalid: " + specifics);
    }
}

class InvalidCardException extends Throwable {
    InvalidCardException(String specifics) {
        System.out.println("Card is invalid: " + specifics);
    }
}

class InvalidRoomException extends Throwable {
    InvalidRoomException(String specifics) {
        System.out.println("Room is invalid: " + specifics);
    }
}

class InvalidWeaponException extends Throwable {
    InvalidWeaponException(String specifics) {
        System.out.println("Weapon is invalid: " + specifics);
    }
}

class InvalidPlayerException extends Throwable {
    InvalidPlayerException(String specifics) {
        System.out.println("Player is invalid: " + specifics);
    }
}