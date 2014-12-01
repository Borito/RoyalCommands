package org.royaldev.royalcommands.rcommands.teleport;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.shaded.mkremins.fanciful.FancyMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeleportRequest {

    // NOTE: Not using UUIDs, because this is so temporary that it should NOT matter. It will just add lag time.
    private final static Map<String, List<TeleportRequest>> teleportRequests = new HashMap<>();
    private final String requester;
    private final String target;
    private final TeleportType teleportType;
    private final long date;

    /**
     * Constructs a teleport request. This does not register it in the list of pending requests.
     *
     * @param requester    Requester's name
     * @param target       Target's name
     * @param teleportType Type of teleport
     * @param date         Time teleport was requested
     */
    public TeleportRequest(final String requester, final String target, final TeleportType teleportType, final long date) {
        this.requester = requester;
        this.target = target;
        this.teleportType = teleportType;
        this.date = date;
    }

    /**
     * Gets the first registered TeleportRequest with the given requester and target names.
     *
     * @param requester Requester's name
     * @param target    Target's name
     * @return First matching TeleportRequest or null
     */
    public static TeleportRequest getFirstRequest(final String requester, final String target) {
        final List<TeleportRequest> trs = TeleportRequest.getRequests().get(target);
        if (trs == null) return null;
        for (TeleportRequest tr : trs) {
            if (tr == null || !tr.getRequester().equalsIgnoreCase(requester) || !tr.getTarget().equalsIgnoreCase(target))
                continue;
            return tr;
        }
        return null;
    }

    /**
     * Gets the latest request for the given target name. If there are no requests, this will return null.
     *
     * @param target Target's name
     * @return Latest TeleportRequest or null
     */
    public static TeleportRequest getLatestRequest(final String target) {
        final List<TeleportRequest> trs = TeleportRequest.getRequests().get(target);
        if (trs == null) return null;
        long highest = -1L;
        TeleportRequest latest = null;
        for (TeleportRequest tr : trs) {
            if (tr == null || !tr.getTarget().equalsIgnoreCase(target)) continue;
            if (highest == -1L || tr.getDate() > highest) {
                highest = tr.getDate();
                latest = tr;
            }
        }
        return latest;
    }

    /**
     * Gets all teleport requests currently pending.
     *
     * @return Map&lt;Target Name, List&lt;TeleportRequest&gt;&gt;
     */
    public static Map<String, List<TeleportRequest>> getRequests() {
        return TeleportRequest.teleportRequests;
    }

    /**
     * Checks to see if a request with the given requester and target names exists, regardless of the request type.
     *
     * @param requester Requester's name
     * @param target    Target's name
     * @return true if requests exist, false if not
     */
    private static boolean hasPendingRequest(final String requester, final String target) {
        final List<TeleportRequest> trs = TeleportRequest.getRequests().get(target);
        if (trs == null) return false;
        for (TeleportRequest tr : trs) {
            if (tr == null || !tr.getRequester().equalsIgnoreCase(requester) || !tr.getTarget().equalsIgnoreCase(target))
                continue;
            return true;
        }
        return false;
    }

    /**
     * Sends a teleport request to the target from the requester.
     *
     * @param requester    Requester of the teleport
     * @param target       Target of the teleport
     * @param teleportType Type of teleport
     * @param confirmation If a confirmation of the request should be sent to the requester
     */
    public static void send(final Player requester, final Player target, final TeleportType teleportType, final boolean confirmation) {
        if (requester.getName().equalsIgnoreCase(target.getName())) {
            requester.sendMessage(MessageColor.NEGATIVE + "You cannot teleport to yourself.");
            return;
        }
        final TeleportRequest tr = new TeleportRequest(requester.getName(), target.getName(), teleportType, System.currentTimeMillis());
        List<TeleportRequest> trs;
        synchronized (TeleportRequest.teleportRequests) {
            trs = TeleportRequest.teleportRequests.get(target.getName());
        }
        if (trs == null) trs = new ArrayList<>();
        if (TeleportRequest.hasPendingRequest(requester.getName(), target.getName())) {
            requester.sendMessage(MessageColor.NEGATIVE + "You already have a request pending with " + MessageColor.NEUTRAL + target.getName() + MessageColor.NEGATIVE + ".");
            return;
        }
        trs.add(tr);
        synchronized (TeleportRequest.teleportRequests) {
            TeleportRequest.teleportRequests.put(target.getName(), trs);
        }
        target.sendMessage(teleportType.getMessage(requester));
        // @formatter:off
        new FancyMessage("To accept, use ")
                .color(MessageColor.POSITIVE._())
            .then("/tpaccept")
                .color(MessageColor.NEUTRAL._())
                .tooltip("Click here to execute this command.")
                .command("/tpaccept")
            .then(". To decline, use ")
                .color(MessageColor.POSITIVE._())
            .then("/tpdeny")
                .color(MessageColor.NEUTRAL._())
                .tooltip("Click here to execute this command.")
                .command("/tpdeny")
            .then(".")
                .color(MessageColor.POSITIVE._())
            .send(target);
        // @formatter:on
        if (confirmation)
            requester.sendMessage(MessageColor.POSITIVE + "Request sent to " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
    }

    /**
     * Sends a teleport request to the target from the requester.
     *
     * @param requester    Requester of the teleport
     * @param target       Target of the teleport
     * @param teleportType Type of teleport
     */
    public static void send(final Player requester, final Player target, final TeleportType teleportType) {
        TeleportRequest.send(requester, target, teleportType, true);
    }

    /**
     * Accepts this TeleportRequest and attempts the teleport. Also sends confirmation messages to both parties.
     */
    public void accept() {
        this.expire();
        final Player requester = Bukkit.getPlayerExact(this.getRequester());
        final Player target = Bukkit.getPlayerExact(this.getTarget());
        if (requester == null || target == null) return;
        String error;
        switch (this.getType()) {
            case TO:
                error = RUtils.teleport(requester, target);
                break;
            case HERE:
                error = RUtils.teleport(target, requester);
                break;
            default:
                return;
        }
        final boolean success = error.isEmpty();
        final String message = MessageColor.POSITIVE + "The request to teleport " + MessageColor.NEUTRAL + ((this.getType() == TeleportType.TO) ? requester.getName() : target.getName()) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + ((this.getType() == TeleportType.TO) ? target.getName() : requester.getName()) + MessageColor.POSITIVE + " was accepted.";
        if (success) {
            requester.sendMessage(message);
            target.sendMessage(message);
        } else {
            final String errorMessage = message.substring(0, message.length() - 1).replace(MessageColor.POSITIVE.toString(), MessageColor.NEGATIVE.toString()) + ", but there was an error.";
            requester.sendMessage(errorMessage);
            requester.sendMessage(MessageColor.NEGATIVE + error);
            target.sendMessage(errorMessage);
            target.sendMessage(MessageColor.NEGATIVE + error);
        }
    }

    /**
     * Denies the teleport request and {@link #expire()}s it. Also sends confirmation messages to both parties.
     */
    public void deny() {
        this.expire();
        final Player requester = Bukkit.getPlayerExact(this.getRequester());
        final Player target = Bukkit.getPlayerExact(this.getTarget());
        final String message = MessageColor.POSITIVE + "The request to teleport " + MessageColor.NEUTRAL + ((this.getType() == TeleportType.TO) ? this.getRequester() : this.getTarget()) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + ((this.getType() == TeleportType.TO) ? this.getTarget() : this.getRequester()) + MessageColor.POSITIVE + " was denied.";
        if (requester != null)
            requester.sendMessage(message.replace(MessageColor.POSITIVE.toString(), MessageColor.NEGATIVE.toString()));
        if (target != null) target.sendMessage(message);
    }

    /**
     * Causes this TeleportRequest to expire, removing it from the registered list of requests.
     */
    public void expire() {
        final List<TeleportRequest> trs = TeleportRequest.getRequests().get(this.getTarget());
        if (trs == null) return; // not registered, then
        trs.remove(this);
        TeleportRequest.getRequests().put(this.getRequester(), trs);
    }

    public long getDate() {
        return this.date;
    }

    public String getRequester() {
        return this.requester;
    }

    public String getTarget() {
        return this.target;
    }

    public TeleportType getType() {
        return this.teleportType;
    }

    public enum TeleportType {
        /**
         * Teleporting the requester to the target.
         */
        TO(MessageColor.NEUTRAL + "%s" + MessageColor.POSITIVE + " would like to teleport to you."),
        /**
         * Teleporting the target to the requester.
         */
        HERE(MessageColor.NEUTRAL + "%s" + MessageColor.POSITIVE + " would like you to teleport to them.");

        private final String requestMessage;

        TeleportType(final String requestMessage) {
            this.requestMessage = requestMessage;
        }

        /**
         * Formats the output of {@link #getRequestMessage()} with the given name.
         *
         * @param name Name to insert into the message
         * @return Formatted message
         */
        public String getMessage(final String name) {
            return String.format(this.requestMessage, name);
        }

        /**
         * Formats the output of {@link #getRequestMessage()} with the name of the given CommandSender.
         *
         * @param cs CommandSender to get name from to insert into the message
         * @return Formatted message
         */
        public String getMessage(final CommandSender cs) {
            return this.getMessage(cs.getName());
        }

        /**
         * Gets the message to send to the target. Must be formatted using {@link java.lang.String#format} to insert the
         * requester's name.
         *
         * @return Unformatted message
         */
        public String getRequestMessage() {
            return this.requestMessage;
        }
    }
}
