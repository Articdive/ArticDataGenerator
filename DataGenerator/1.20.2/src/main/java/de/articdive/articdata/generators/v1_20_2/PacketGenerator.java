package de.articdive.articdata.generators.v1_20_2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GeneratorEntry(name = "Packet ID", supported = true)
@GeneratorEntry(name = "Packet Classname", supported = true)
@GeneratorEntry(name = "Packet Direction", supported = true)
@GeneratorEntry(name = "Connection State", supported = true)
public final class PacketGenerator extends DataGenerator<Class<? extends Packet<?>>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PacketGenerator.class);

    private final Map<ConnectionProtocol, Map<PacketFlow, ConnectionProtocol.CodecData<?>>> connectionProtocolPacketSets = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public PacketGenerator() {
        for (ConnectionProtocol cp : ConnectionProtocol.values()) {
            connectionProtocolPacketSets.put(cp, ReflectionHelper.getHiddenField(Map.class, "flows", ConnectionProtocol.class, cp));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void generateNames() {
        for (Map.Entry<ConnectionProtocol, Map<PacketFlow, ConnectionProtocol.CodecData<?>>> connectionProtocolMapEntry : connectionProtocolPacketSets.entrySet()) {
            Map<PacketFlow, ConnectionProtocol.CodecData<?>> flows = connectionProtocolMapEntry.getValue();
            for (PacketFlow packetFlow : PacketFlow.values()) {
                ConnectionProtocol.CodecData<?> codecData = flows.get(packetFlow);
                if (codecData != null) {
                    Int2ObjectMap<Class<? extends Packet<?>>> packetMap = ReflectionHelper.callHiddenMethod(Int2ObjectMap.class, "packetsByIds", ConnectionProtocol.CodecData.class, codecData, new HashMap<>());
                    if (packetMap == null) {
                        LOGGER.error("Skipping packet flow '" + packetFlow.name() + "' received null for the PacketSet.");
                        continue;
                    }
                    for (Int2ObjectMap.Entry<Class<? extends Packet<?>>> packet : packetMap.int2ObjectEntrySet()) {
                        names.put(packet.getValue(), packet.getValue().getSimpleName());
                    }
                }
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject packets = new JsonObject();
        for (Map.Entry<ConnectionProtocol, Map<PacketFlow, ConnectionProtocol.CodecData<?>>> entry : connectionProtocolPacketSets.entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().id())).toList()) {
            JsonObject connectionProtocolSection = new JsonObject();
            connectionProtocolSection.addProperty("ordinal", entry.getKey().ordinal());
            connectionProtocolSection.addProperty("id", entry.getKey().id());

            Map<PacketFlow, ConnectionProtocol.CodecData<?>> flows = entry.getValue();
            for (PacketFlow packetFlow : PacketFlow.values()) {
                JsonArray flowSection = new JsonArray();
                // PacketSet
                ConnectionProtocol.CodecData codecData = flows.get(packetFlow);

                if (codecData != null) {
                    // PacketSet contains a field 'Object2IntMap<Class<? extends Packet<T>>> classToId'
                    @SuppressWarnings("unchecked")
                    Int2ObjectMap<Class<? extends Packet<?>>> packetMap = ReflectionHelper.callHiddenMethod(Int2ObjectMap.class, "packetsByIds", ConnectionProtocol.CodecData.class, codecData, new HashMap<>());

                    if (packetMap == null) {
                        LOGGER.error("Skipping packet flow '" + packetFlow.name() + "' received null for the PacketSet.");
                        continue;
                    }

                    for (Int2ObjectMap.Entry<Class<? extends Packet<?>>> packet : packetMap.int2ObjectEntrySet().stream().sorted(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey)).toList()) {
                        JsonObject packetSection = new JsonObject();

                        packetSection.addProperty("name", names.get(packet.getValue()));
                        packetSection.addProperty("id", packet.getIntKey());

                        flowSection.add(packetSection);
                    }
                }
                connectionProtocolSection.add(packetFlow.name(), flowSection);
            }

            packets.add(entry.getKey().name(), connectionProtocolSection);
        }
        return packets;
    }
}
