/**
 * Classes containing any potential errors
 */

class InvalidMoveException extends Throwable {
    InvalidMoveException(String specifics) {
        System.out.println("Move is invalid: " + specifics);
    }
}

class InvalidFileException extends Throwable {
    InvalidFileException(String specifics) {
        System.out.println("Cannot find file: " + specifics);
    }
}