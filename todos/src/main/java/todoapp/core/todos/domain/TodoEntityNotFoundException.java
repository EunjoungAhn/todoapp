package todoapp.core.todos.domain;

/**
 * 할 일 저장소에서 할 일 엔티티를 찾을 수 없을 때 발생 가능한 예외 클래스
 *
 * @author springrunner.kr@gmail.com
 */
public class TodoEntityNotFoundException extends TodoEntityException{

    private static final long serialVersionUID = 1L;
    
    private final Long id;

    public TodoEntityNotFoundException(Long id) {
        super("Todo 엔티티를 찾을 수 없습니다. (id: %d)", id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    //삭제를 하려는 할일의 id 값을 가져오는 오류 메시지 설정
	@Override
	public Object[] getArguments() {
		//스프링이 제공하는 메세지 소스에서는 숫자값이 넘어가면 콤마와 같은 처리를 함으로 스트링으로 감싸준다.
		return new Object[] { String.valueOf(id) };
	}

}