package com.grizbenzis.bgj10.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by sponaas on 1/22/16.
 */
public class RenderComponent extends Component {

    public int order;

    public RenderComponent(int orderInit) { order = orderInit; }

}
