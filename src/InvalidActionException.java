/**
 * Classes containing any potential errors
 */

public class InvalidActionException extends Throwable {
    InvalidActionException(String specifics) {
        System.out.println("Move is invalid: " + specifics);
    }
}

class InvalidCardExcdption extends Throwable {
    InvalidCardExcdption(String specifics) {
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
