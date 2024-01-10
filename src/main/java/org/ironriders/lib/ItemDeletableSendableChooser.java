package org.ironriders.lib;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

public class ItemDeletableSendableChooser<V> implements Sendable, AutoCloseable {
    /**
     * The key for the default value.
     */
    private static final String DEFAULT = "default";

    /**
     * The key for the selected option.
     */
    private static final String SELECTED = "selected";

    /**
     * The key for the active option.
     */
    private static final String ACTIVE = "active";

    /**
     * The key for the option array.
     */
    private static final String OPTIONS = "options";

    /**
     * The key for the instance number.
     */
    private static final String INSTANCE = ".instance";
    private static final AtomicInteger s_instances = new AtomicInteger();
    /**
     * A map linking strings to the objects they represent.
     */
    private final Map<String, V> m_map = new LinkedHashMap<>();
    private final int m_instance;
    private final ReentrantLock m_mutex = new ReentrantLock();
    private String m_defaultChoice = "";
    private String m_previousVal;
    private Consumer<V> m_listener;
    private String m_selected;

    @SuppressWarnings("this-escape")
    public ItemDeletableSendableChooser() {
        m_instance = s_instances.getAndIncrement();
        SendableRegistry.add(this, "SendableChooser", m_instance);
    }

    @Override
    public void close() {
        SendableRegistry.remove(this);
    }

    public void addOption(String name, V object) {
        m_map.put(name, object);
    }

    public void removeOption(String name) {
        m_map.remove(name);
    }

    public void setDefaultOption(String name, V object) {
        requireNonNullParam(name, "name", "setDefaultOption");

        m_defaultChoice = name;
        addOption(name, object);
    }

    public V getSelected() {
        m_mutex.lock();
        try {
            if (m_selected != null) {
                return m_map.get(m_selected);
            } else {
                return m_map.get(m_defaultChoice);
            }
        } finally {
            m_mutex.unlock();
        }
    }

    public void onChange(Consumer<V> listener) {
        requireNonNullParam(listener, "listener", "onChange");
        m_mutex.lock();
        m_listener = listener;
        m_mutex.unlock();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("String Chooser");
        builder.publishConstInteger(INSTANCE, m_instance);
        builder.addStringProperty(DEFAULT, () -> m_defaultChoice, null);
        builder.addStringArrayProperty(OPTIONS, () -> m_map.keySet().toArray(new String[0]), null);
        builder.addStringProperty(
                ACTIVE,
                () -> {
                    m_mutex.lock();
                    try {
                        if (m_selected != null) {
                            return m_selected;
                        } else {
                            return m_defaultChoice;
                        }
                    } finally {
                        m_mutex.unlock();
                    }
                },
                null);
        builder.addStringProperty(
                SELECTED,
                null,
                val -> {
                    V choice;
                    Consumer<V> listener;
                    m_mutex.lock();
                    try {
                        m_selected = val;
                        if (!m_selected.equals(m_previousVal) && m_listener != null) {
                            choice = m_map.get(val);
                            listener = m_listener;
                        } else {
                            choice = null;
                            listener = null;
                        }
                        m_previousVal = val;
                    } finally {
                        m_mutex.unlock();
                    }
                    if (listener != null) {
                        listener.accept(choice);
                    }
                });
    }
}
