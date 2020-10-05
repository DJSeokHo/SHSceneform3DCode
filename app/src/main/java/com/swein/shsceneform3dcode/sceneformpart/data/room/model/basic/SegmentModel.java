package com.swein.shsceneform3dcode.sceneformpart.data.room.model.basic;


import com.google.ar.sceneform.Node;

public class SegmentModel {

    public Node segmentNode;
    public float length = 0;

    public SegmentModel(Node segmentNode, float length) {
        this.segmentNode = segmentNode;
        this.length = length;
    }

    public void clear() {
        if(segmentNode != null) {
            segmentNode.setParent(null);
            segmentNode = null;
        }

        length = 0;
    }
}
