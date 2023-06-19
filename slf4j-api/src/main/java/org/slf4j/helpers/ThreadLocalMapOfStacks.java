package org.slf4j.helpers;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple implementation of ThreadLocal backed Map containing values of type 
 * Deque<String>.
 * 
 * @author Ceki Guuml;c&uuml;
 * @since 2.0.0
 */
public class ThreadLocalMapOfStacks {

    // BEWARE: Keys or values placed in a ThreadLocal should not be of a type/class
    // not included in the JDK. See also https://jira.qos.ch/browse/LOGBACK-450

    final ThreadLocal<Map<String, Deque<String>>> tlMapOfStacks = new ThreadLocal<>();
    {
        tlMapOfStacks.set(new HashMap<>());
    }

    public void pushByKey(String key, String value) {
        if (key == null)
            return;

        Deque<String> deque = tlMapOfStacks.get().get(key);
        if (deque == null) {
            deque = new ArrayDeque<>();
        }

        deque.push(value);
        tlMapOfStacks.get().put(key, deque);
    }

    public String popByKey(String key) {
        Deque<String> deque = getExistingDeque(key);

        return deque == null ? null : deque.pop();
    }

    public Deque<String> getCopyOfDequeByKey(String key) {
        Deque<String> deque = getExistingDeque(key);

        return deque == null ? null : new ArrayDeque<>(deque);
    }

    private Deque<String> getExistingDeque(String key) {
        if(key == null) {
            return null;
        }

        return tlMapOfStacks.get().get(key);
    }
    
    /**
     * Clear the deque(stack) referenced by 'key'. 
     * 
     * @param key identifies the  stack
     * 
     * @since 2.0.0
     */
    public void clearDequeByKey(String key) {
        Deque<String> deque = getExistingDeque(key);
        if(deque != null) {
            deque.clear();
        }
    }

}
