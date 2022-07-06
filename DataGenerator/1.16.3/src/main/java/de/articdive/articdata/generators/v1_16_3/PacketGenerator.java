package de.articdive.articdata.generators.v1_16_3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.articdive.articdata.datagen.DataGenerator;
import de.articdive.articdata.datagen.ReflectionHelper;
import de.articdive.articdata.datagen.annotations.GeneratorEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
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

    private final Class<?> packetSetClass;
    // The '?' is a PacketSet, private class in the Mojang JAR.
    private final Map<ConnectionProtocol, Map<PacketFlow, ?>> connectionProtocolPacketSets = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public PacketGenerator() {
        Optional<Class<?>> packetSetClassO = Arrays.stream(ConnectionProtocol.class.getDeclaredClasses()).filter(aClass -> aClass.getSimpleName().equals("PacketSet")).findFirst();
        if (packetSetClassO.isEmpty()) {
            throw new IllegalStateException("Could not find 'PacketSet' Class.");
        }
        packetSetClass = packetSetClassO.get();
        for (ConnectionProtocol cp : ConnectionProtocol.values()) {
            connectionProtocolPacketSets.put(cp, ReflectionHelper.getHiddenField(Map.class, "flows", ConnectionProtocol.class, cp));
        }
    }

    @Override
    public void generateNames() {
        for (Map.Entry<ConnectionProtocol, Map<PacketFlow, ?>> connectionProtocolMapEntry : connectionProtocolPacketSets.entrySet()) {
            Map<PacketFlow, ?> flows = connectionProtocolMapEntry.getValue();
            for (PacketFlow packetFlow : PacketFlow.values()) {
                // PacketSet
                Object packetSet = flows.get(packetFlow);
                if (packetSet != null) {
                    // PacketSet contains a field 'Object2IntMap<Class<? extends Packet<T>>> classToId'
                    @SuppressWarnings("unchecked")
                    Object2IntMap<Class<? extends Packet<?>>> packetMap = ReflectionHelper.getHiddenField(Object2IntMap.class, "classToId", packetSetClass.asSubclass(Object.class), packetSet);
                    if (packetMap == null) {
                        LOGGER.error("Skipping packet flow '" + packetFlow.name() + "' received null for the PacketSet.");
                        continue;
                    }
                    for (Object2IntMap.Entry<Class<? extends Packet<?>>> packet : packetMap.object2IntEntrySet()) {
                        names.put(packet.getKey(), packet.getKey().getSimpleName());
                    }
                }
            }
        }
    }

    @Override
    public JsonObject generate() {
        JsonObject packets = new JsonObject();
        for (Map.Entry<ConnectionProtocol, Map<PacketFlow, ?>> entry : connectionProtocolPacketSets.entrySet().stream().sorted(Comparator.comparingInt(o -> o.getKey().getId())).toList()) {
            JsonObject connectionProtocolSection = new JsonObject();
            connectionProtocolSection.addProperty("id", entry.getKey().getId());

            Map<PacketFlow, ?> flows = entry.getValue();
            for (PacketFlow packetFlow : PacketFlow.values()) {
                JsonArray flowSection = new JsonArray();
                // PacketSet
                Object packetSet = flows.get(packetFlow);

                if (packetSet != null) {
                    // PacketSet contains a field 'Object2IntMap<Class<? extends Packet<T>>> classToId'
                    @SuppressWarnings("unchecked")
                    Object2IntMap<Class<? extends Packet<?>>> packetMap = ReflectionHelper.getHiddenField(Object2IntMap.class, "classToId", packetSetClass.asSubclass(Object.class), packetSet);
                    if (packetMap == null) {
                        LOGGER.error("Skipping packet flow '" + packetFlow.name() + "' received null for the PacketSet.");
                        continue;
                    }
                    for (Object2IntMap.Entry<Class<? extends Packet<?>>> packet : packetMap.object2IntEntrySet().stream().sorted(Comparator.comparingInt(Object2IntMap.Entry::getIntValue)).toList()) {
                        JsonObject packetSetion = new JsonObject();

                        packetSetion.addProperty("name", names.get(packet.getKey()));
                        packetSetion.addProperty("id", packet.getIntValue());

                        flowSection.add(packetSetion);
                    }
                }
                connectionProtocolSection.add(packetFlow.name(), flowSection);
            }

            packets.add(entry.getKey().name(), connectionProtocolSection);
        }
        return packets;
    }
}
