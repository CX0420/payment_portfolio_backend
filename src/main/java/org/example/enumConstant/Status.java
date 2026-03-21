package org.example.enumConstant;

/**
 * Implementers should provide a set of statuses to be used by the system.
 *
 * @author ChristopherChak
 */
public interface Status {

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    int getCode();

    /**
     * Gets the status code in string format.
     *
     * @return the status code in string format
     */
    String getCodeInStringFormat();

    /**
     * Gets the associated text message.
     *
     * @return the associated text message
     */
    String getMessage();

    /**
     * Gets the name of the status, usually the name of the implementer Enum.
     *
     * @return the name of the status
     */
    String getName();

    /**
     * Gets the associated messinging key.
     */
    String getMessageKey();

}
