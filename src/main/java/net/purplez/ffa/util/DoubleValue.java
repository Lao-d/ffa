package net.purplez.ffa.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class DoubleValue<F, S> {

    private F first;
    private S second;

}
