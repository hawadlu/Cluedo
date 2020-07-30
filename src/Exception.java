/**
 * Classes containing any potential errors
 */

class InvalidMoveException extends Throwable {
    InvalidMoveException(String specifics) {
        System.out.println("Move is invalid: " + specifics);
    }
}