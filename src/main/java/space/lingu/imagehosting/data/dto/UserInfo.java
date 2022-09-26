package space.lingu.imagehosting.data.dto;

import space.lingu.light.DataColumn;

/**
 * Fragment of {@link space.lingu.imagehosting.data.entity.User}
 *
 * @author RollW
 */
public record UserInfo(
        @DataColumn(name = "user_id")
        long id,

        @DataColumn(name = "user_name")
        String username,

        @DataColumn(name = "user_email")
        String email
) {
}
