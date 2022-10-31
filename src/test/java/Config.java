import dev.refinedtech.configure.metadata.comment.Comment;

public interface Config {

    @Comment("The name of the thing")
    @Comment("Default value: ${DEFAULT}")
    default String getName() {
        return "DefaultName";
    }

}