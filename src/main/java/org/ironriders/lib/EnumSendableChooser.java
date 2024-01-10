package org.ironriders.lib;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class EnumSendableChooser<E extends Enum<?>> extends SendableChooser<E> {
    public EnumSendableChooser(Class<E> enumType, E defaultOption, String name) {
        E[] options = enumType.getEnumConstants();

        for (E option : options) {
            addOption(option.name(), option);
        }
        setDefaultOption(defaultOption.name(), defaultOption);

        SmartDashboard.putData(name, this);
    }
}
