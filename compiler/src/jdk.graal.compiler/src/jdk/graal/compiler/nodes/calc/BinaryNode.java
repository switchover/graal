/*
 * Copyright (c) 2009, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.graal.compiler.nodes.calc;

import jdk.graal.compiler.core.common.type.Stamp;
import jdk.graal.compiler.graph.NodeClass;
import jdk.graal.compiler.nodes.NodeView;
import jdk.graal.compiler.nodes.ValueNode;
import jdk.graal.compiler.nodes.spi.Canonicalizable;
import jdk.graal.compiler.nodeinfo.NodeInfo;

/**
 * The {@code BinaryNode} class is the base of arithmetic and logic operations with two inputs.
 */
@NodeInfo
public abstract class BinaryNode extends FloatingNode implements Canonicalizable.Binary<ValueNode> {

    public static final NodeClass<BinaryNode> TYPE = NodeClass.create(BinaryNode.class);
    @Input protected ValueNode x;
    @Input protected ValueNode y;

    @Override
    public ValueNode getX() {
        return x;
    }

    @Override
    public ValueNode getY() {
        return y;
    }

    public void setX(ValueNode x) {
        updateUsages(this.x, x);
        this.x = x;
    }

    public void setY(ValueNode y) {
        updateUsages(this.y, y);
        this.y = y;
    }

    /**
     * Creates a new BinaryNode instance.
     *
     * @param stamp the result type of this instruction
     * @param x the first input instruction
     * @param y the second input instruction
     */
    protected BinaryNode(NodeClass<? extends BinaryNode> c, Stamp stamp, ValueNode x, ValueNode y) {
        super(c, stamp);
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean inferStamp() {
        return updateStamp(foldStamp(getX().stamp(NodeView.DEFAULT), getY().stamp(NodeView.DEFAULT)));
    }

    /**
     * Compute an improved for this node using the passed in stamps. The stamps must be compatible
     * with the current values of {@link #x} and {@link #y}. This code is used to provide the
     * default implementation of {@link #inferStamp()} and may be used by external optimizations.
     *
     * @param stampX
     * @param stampY
     */
    public abstract Stamp foldStamp(Stamp stampX, Stamp stampY);
}
