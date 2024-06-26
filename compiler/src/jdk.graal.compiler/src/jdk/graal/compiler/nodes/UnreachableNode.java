/*
 * Copyright (c) 2012, 2021, Oracle and/or its affiliates. All rights reserved.
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
package jdk.graal.compiler.nodes;

import static jdk.graal.compiler.nodeinfo.NodeCycles.CYCLES_IGNORED;
import static jdk.graal.compiler.nodeinfo.NodeSize.SIZE_IGNORED;

import jdk.graal.compiler.core.common.type.StampFactory;
import jdk.graal.compiler.graph.NodeClass;
import jdk.graal.compiler.nodeinfo.NodeInfo;
import jdk.graal.compiler.nodes.spi.Simplifiable;
import jdk.graal.compiler.nodes.spi.SimplifierTool;

/**
 * Node for marking a branch in a snippet as unreachable. Usage example:
 *
 * <pre>
 * void mySnippet() {
 *     callThatNeverReturns();
 *     throw UnreachableNode.unreachable();
 * }
 * </pre>
 *
 * See {@link DeadEndNode} for more details.
 */
@NodeInfo(size = SIZE_IGNORED, cycles = CYCLES_IGNORED)
public final class UnreachableNode extends FixedWithNextNode implements Simplifiable {
    public static final NodeClass<UnreachableNode> TYPE = NodeClass.create(UnreachableNode.class);

    public UnreachableNode() {
        super(TYPE, StampFactory.object());
    }

    @Override
    public void simplify(SimplifierTool tool) {
        tool.deleteBranch(next());
        replaceAtPredecessor(graph().add(new DeadEndNode()));
        safeDelete();
    }

    @NodeIntrinsic
    public static native RuntimeException unreachable();
}
