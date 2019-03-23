package tk.rdvdev2.tfaddon;

import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.tensorflow.Graph;
import org.tensorflow.Session;

import javax.annotation.Nullable;

/**
 * Expose this capability to attach TensorFlow sessions to CapabilityProviders
 */
public interface ITensorFlowCapability {

    /**
     * @return The current TensorFlow Session
     */
    Session getSession();

    /**
     * @return The current TensorFlow Graph
     */
    Graph getGraph();

    /**
     * Assign a new TensorFlow Graph
     * @param graph
     */
    void setGraph(Graph graph);

    /**
     * Close the TensorFlow Session
     */
    void closeSession();

    /**
     * Reload the TensorFlow Session
     */
    void reloadSession();

    class Storage implements Capability.IStorage<ITensorFlowCapability> {

        @Nullable
        @Override
        public INBTBase writeNBT(Capability<ITensorFlowCapability> capability, ITensorFlowCapability instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setByteArray("graph", instance.getGraph().toGraphDef());
            return compound;
        }

        @Override
        public void readNBT(Capability<ITensorFlowCapability> capability, ITensorFlowCapability instance, EnumFacing side, INBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbt;
                Graph graph = new Graph();
                graph.importGraphDef(((NBTTagCompound) nbt).getByteArray("graph"));
                instance.setGraph(graph);
            }
        }
    }

    /**
     * Default implementation of the ITensorFlowCapability
     */
    class Impl implements ITensorFlowCapability {
        private Session session;
        private Graph graph;

        public Impl() {
            this(new Graph());
        }

        public Impl(Graph graph) {
            this.graph = graph;
            session = new Session(graph);
            onContentsChanged();
        }

        @Override
        public Session getSession() {
            return session;
        }

        @Override
        public Graph getGraph() {
            return graph;
        }

        @Override
        public void setGraph(Graph graph) {
            this.graph = graph;
            reloadSession();
        }

        @Override
        public void closeSession() {
            session.close();
            onContentsChanged();
        }

        @Override
        public void reloadSession() {
            closeSession();
            session = new Session(graph);
            onContentsChanged();
        }

        /**
         * Should be overwritten to call world.markDirty() if the CapabilityProvider doesn't do it.
         */
        public void onContentsChanged() {

        }
    }
}
