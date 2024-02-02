

# 테스트 코드의 부재

## 상황

> 사이드 프로젝트를 개발할 때 테스트코드를 작성하지 않고 무작정 개발을 진행하였습니다. 왜냐하면 테스트 코드를
> 작성하다가는 개발 속도가 느릴 것 같았기 때문입니다. 
> 
> 그래서 기능을 개발할때마다 서버를 기동하면서 일일이 기능이 잘 되는지 계속 확인하면서 개발했습니다. 
> 하지만 점점 개발을 진행하면서 프로젝트가 커지고 기능이 점점 늘어나면서 확인하는 시간이 증가했습니다. 
> 혹은 기능을 확인할 때 깜빡하고 확인하지 않은 기능도 있어서 나중에 해당 기능이 제대로 
> 동작하지 않는 경우를 나중에 인지하는 경우도 있었습니다. 프로젝트를 진행하면 점점 규모가 커지게 될텐데, 개발을 진행하면서 테스트 코드의 필요성을 크게 느꼈습니다.

### 해결 방법 : 테스트 코드 작성

> 결국 테스트 코드를 작성하는 것이 결국 장기적으로 볼 때, 시간 단축 및 견고한 소프트웨어를 개발하는데 필수라는 생각이
> 들어 Junit5로 테스트 코드를 작성했습니다.

<img src="./image/problem4/problem-4-173654.png">
<br>
- 수정 시 다른 기능에 문제가 발생하는 지 빠른 시간내에 파악할 수 있다.

### 결론

> 테스트 코드를 작성하여 코드 수정 시, 발생하는 side effect 를 체크하는데 걸리는 시간을
> 감소하여 수월하게 리팩토링을 할 수 있었습니다.