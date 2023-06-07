// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class NewYear {
    private final EmbedBuilder eb;

    public NewYear() {
        this.eb = new EmbedBuilder();
    }

    public MessageCreateData startMessage() {
        this.eb.setTitle("\u041f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u044f\u0448\u043a\u0438");
        this.eb.setDescription("\u0412\u044b \u043c\u043e\u0436\u0435\u0442\u0435 \u043f\u043e\u0437\u0434\u0440\u0430\u0432\u0438\u0442\u044c \u0441\u0432\u043e\u0435\u0433\u043e \u0434\u0440\u0443\u0433\u0430/\u0432\u0442\u043e\u0440\u0443\u044e \u043f\u043e\u043b\u043e\u0432\u0438\u043d\u043a\u0443. \u041c\u043e\u0436\u0435\u0442\u0435 \u0443\u043a\u0430\u0437\u0430\u0442\u044c \u0430\u0432\u0442\u043e\u0440\u0430 \u0438\u043b\u0438 \u0430\u043d\u043e\u043d\u0438\u043c\u043d\u043e. \n \u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u043a\u043d\u043e\u043f\u043a\u0443 \u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u0438 \u0437\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u0435 \u043f\u043e\u043b\u044f. \n \u0417\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043e \u043e\u0441\u043a\u043e\u0440\u0431\u043b\u044f\u0442\u044c \u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0435\u0439, \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u0431\u0430\u043d\u0432\u043e\u0440\u0434\u044b \u0438\u043b\u0438 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u044f\u0442\u044c 18+ \u043a\u043e\u043d\u0442\u0435\u043d\u0442");
        this.eb.setThumbnail("https://media.discordapp.net/attachments/1044490570174824518/1056606585889230909/tumblr_otr0miRDLw1u0xk60o2_r1_500.gif");
        final Button sendButton = Button.secondary("send", "\u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c");
        final MessageCreateData msg = new MessageCreateBuilder().addEmbeds(this.eb.build()).setActionRow(sendButton).build();
        return msg;
    }

    public void buttonEvent(final ButtonInteractionEvent event) {
        final TextInput nameSender = TextInput.create("nameSender", "\u0412\u0430\u0448\u0435 \u0438\u043c\u044f", TextInputStyle.SHORT).setPlaceholder("\u041f\u043e\u0441\u0442\u0430\u0432\u044c\u0442\u0435 \u043f\u0440\u043e\u0447\u0435\u0440\u043a, \u043b\u0438\u0431\u043e \u043d\u0430\u043f\u0438\u0448\u0438\u0442\u0435 \u0410\u043d\u043e\u043d\u0438\u043c\u043d\u043e").setMaxLength(100).build();
        final TextInput nameGiver = TextInput.create("nameGiver", "\u0418\u043c\u044f \u041f\u043e\u043b\u0443\u0447\u0430\u0442\u0435\u043b\u044f", TextInputStyle.SHORT).setPlaceholder("\u041d\u0430\u043f\u0438\u0448\u0438\u0442\u0435 \u043d\u0438\u043a \u043a\u043e\u043c\u0443 \u0445\u043e\u0442\u0438\u0442\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c").build();
        final TextInput body = TextInput.create("body", "\u0422\u0435\u043a\u0441\u0442 \u043f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u044f", TextInputStyle.PARAGRAPH).setPlaceholder("\u041d\u0430\u043f\u0438\u0448\u0438\u0442\u0435 \u0432\u0430\u0448\u0438 \u043f\u043e\u0436\u0435\u043b\u0430\u043d\u0438\u044f \u0447\u0435\u043b\u043e\u0432\u0435\u043a\u0443").setMaxLength(1000).build();
        final TextInput image = TextInput.create("image", "\u0421\u0441\u044b\u043b\u043a\u0430 \u043d\u0430 \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0443", TextInputStyle.SHORT).setPlaceholder("\u0421\u0441\u044b\u043b\u043a\u0430 \u043d\u0430 \u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0443 \u0432 \u0444\u043e\u0440\u043c\u0430\u0442\u0435 .png, .jpg, .gif").build();
        final Modal modal = Modal.create("modmail", "\u041f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u0435\u043d\u0438\u0435 \u0441 \u041d\u043e\u0432\u044b\u043c \u0413\u043e\u0434\u043e\u043c").addActionRows(ActionRow.of(nameSender), ActionRow.of(nameGiver), ActionRow.of(body), ActionRow.of(image)).build();
        event.replyModal(modal).queue();
    }

//    public void generateMessage(final String name, final String nameGiver, final String body, final String image, final TextChannel txt, final ModalInteractionEvent event) {
//        final MessageCreateBuilder msg = new MessageCreateBuilder();
//        final Member m = event.getMember();
//        final Optional<Member> memberOptional2 = Objects.requireNonNull(event.getGuild()).getMembersByName(nameGiver, true).stream().findFirst();
//        System.out.println(memberOptional2);
//        final EmbedBuilder eb1 = new EmbedBuilder();
//        try {
//            if (name.equalsIgnoreCase("\u0410\u043d\u043e\u043d\u0438\u043c\u043d\u043e") && memberOptional2.isPresent()) {
//                eb1.setImage(image);
//                msg.addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, memberOptional2.get().getAsMention())).addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, body)).addEmbeds(eb1.build());
//                txt.sendMessage(msg.build()).queue();
//                event.reply("\u041e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e!").setEphemeral(true).queue();
//            }
//            else if (memberOptional2.isPresent()) {
//                eb1.setImage(image);
//                msg.addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, m.getAsMention(), memberOptional2.get().getAsMention())).addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, body)).addEmbeds(eb1.build());
//                txt.sendMessage(msg.build()).queue();
//                event.reply("\u041e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e!").setEphemeral(true).queue();
//            }
//            else {
//                event.reply("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u0438\u043c\u044f \u043f\u043e\u043b\u0443\u0447\u0430\u0442\u0435\u043b\u044f. \u041f\u043e\u043b\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u0441\u043e\u0434\u0435\u0440\u0436\u0430\u0442\u044c \u0422\u0430\u0433 \u0431\u0435\u0437 \u0446\u0438\u0444\u0440").setEphemeral(true).queue();
//            }
//        }
//        catch (IllegalArgumentException e) {
//            if (name.equalsIgnoreCase("\u0410\u043d\u043e\u043d\u0438\u043c\u043d\u043e") && memberOptional2.isPresent()) {
//                msg.addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, memberOptional2.get().getAsMention())).addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, body));
//                txt.sendMessage(msg.build()).queue();
//                event.reply("\u041e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e!").setEphemeral(true).queue();
//            }
//            else if (memberOptional2.isPresent()) {
//                msg.addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, m.getAsMention(), memberOptional2.get().getAsMention())).addContent(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, body));
//                txt.sendMessage(msg.build()).queue();
//                event.reply("\u041e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e!").setEphemeral(true).queue();
//            }
//            else {
//                event.reply("\u041d\u0435\u043f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u043e\u0435 \u0438\u043c\u044f \u043f\u043e\u043b\u0443\u0447\u0430\u0442\u0435\u043b\u044f. \u041f\u043e\u043b\u0435 \u0434\u043e\u043b\u0436\u043d\u043e \u0441\u043e\u0434\u0435\u0440\u0436\u0430\u0442\u044c \u0422\u0430\u0433 \u0431\u0435\u0437 \u0446\u0438\u0444\u0440").setEphemeral(true).queue();
//            }
//        }
//    }
}
