import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.enums.Status

class ApiResponseObserver private constructor(
    val status: Status,
    var t: Any?,
    val error: ErrorResponse?
) {
    companion object {
        fun loading(): ApiResponseObserver {
            return ApiResponseObserver(Status.LOADING, null, null)
        }
        fun success(t: Any?): ApiResponseObserver {
            return ApiResponseObserver(Status.SUCCESS, t, null)
        }
        fun error(error: ErrorResponse?): ApiResponseObserver {
            return ApiResponseObserver(Status.ERROR, null, error)
        }
        fun expire(error: ErrorResponse?): ApiResponseObserver {
            return ApiResponseObserver(Status.EXPIRE, null, error)
        }
    }
}