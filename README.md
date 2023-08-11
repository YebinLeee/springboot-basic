## 📌 CLI 기반 바우처 관리 애플리케이션

## 👩‍💻 요구 사항과 구현 내용 

### 요구사항
- [x]  Maven / Gradle 로 프로젝트를 실제로 구성하고 이때 Spring Boot CLI를 개발PC에 설치해서 명령어들을 사용해보고 프로젝트를 만든다. 그리고 IDE (IntelliJ)에서 실행시켜 본다.
- [x]  바우처 관리 Command-line Application을 만들어본다.
    - 참고: https://dzone.com/articles/interactive-console-applications-in-java
    - [x]  스프링부트 애플리케이션으로 만든다. (Web기능이 없이만듭니다. 즉, 서버가 띄지 않고 커맨드라인 애플리케이션으로 동작해야한다.)
    - [x]  프로그램이 시작하면 다음과 같이 지원가능한 명령어를 알려준다.
    
    ```bash
    === Voucher Program ===
    Type **exit** to exit the program.
    Type **create** to create a new voucher.
    Type **list** to list all vouchers.
    ```
  
- [x]  create / list 커맨드를 지원한다.
    - create 커맨드를 통해 바우처를 생성할수 있다. (FixedAmountVoucher, PercentDiscountVoucher)
    - list 커맨드를 통해 만들어진 바우처를 조회할 수 있다.
    - 바우처 정보를 매모리에 관리한다. 어플리케이션이 종료가 되어 데이터가 모두 사라져도 괜찮습니다. (나중에 영속성을 가지도록 변경할거에요 걱정마세요!)
- [ ]  적절한 로그를 기록하고 `logback` 설정을해서 에러는 파일로 기록된다.
- [ ]  실행가능한 `jar` 파일을 생성한다.

(심화) 파일을 통한 데이터관리 기능과 고객 블랙 리스트 명단 관리기능

- [ ]  메모리 관리가 아닌 파일로 관리가 되는 Repository를 한번 만들어보세요.
    - 기존 메모리 레포지토리는 지우지 말고 개발 프로파일에서만 동작하게 해보세요.
- [ ]  고객 블랙 리스트 명단을 작성한다.
- customer_blacklist.csv 파일을 만들고 스프링 애플리케이션에서 해당 파일을 읽을 수 있고 블랙 리스트조회 할 수있다 (추가할 필요는 없어요. 블랙리스트는 파일로만 관리된다고 가정합니다.)
- [ ]  YAML 프라퍼티를 만들고 어떤 설정을 만들수 있을지 고민해본다.


## 구현 내용
### 전체적인 클래스 구조도  
<img src="https://github.com/prgrms-be-devcourse/springboot-basic/assets/71310074/3435dd2d-13a6-4503-abb5-b0cf28b9efb8" width=700>

- 사용자가 명령어 메뉴로 create을 입력한 경우, `1. 고정 할인 바우처` 또는 `2. 비율 할인 바우처` 중 하나를 선택할 수 있습니다.
   - 1을 입력하여 고정 할인 바우처를 선택하면, 고정 할인 금액을 입력 받습니다. (이 때 입력 값은 1 이상의 정수) 
   - 2를 입력하여 비율 할인 바우처를 선택하면, 할인 비율을 입력 받습니다. (이 때 입력 값은 1 이상 99 이하의 정수)
   - Voucher의Type에 따라 Voucher의 구현체 FixedAmountVoucher, PercentDiscountVoucher가 생성되고, MemoryVoucherRepository의 ConcurrentMap storage에 key(UUID), value(Voucher)에 추가됩니다.
- 사용자가 명령어 메뉴로 list를 입력한 경우, map에 저장되어 있는 바우처 목록이 출력됩니다.
- 시용자가 명령어 메뉴로 exit을 입력한 경우, 프로그램이 종료됩니다.
 
```
=== Voucher Program ===
Type exit to exit the program.
Type create to create a new voucher.
Type list to list all vouchers.

명령 메뉴를 입력해주세요 : create
1: 고정 할인 바우처
2: 비율 할인 바우처
생성할 바우처 타입을 입력해주세요 : 1
할인 고정 금액은 1 이상의 정수여야 합니다. 할인 금액을 입력해주세요 : 2000

명령 메뉴를 입력해주세요 : create
1: 고정 할인 바우처
2: 비율 할인 바우처
생성할 바우처 타입을 입력해주세요 : 2
할인 비율은 1 이상 99 이하의 정수여야 합니다. 할인 비율을 입력해주세요 : 25

명령 메뉴를 입력해주세요 : list
53ca4617-c6ed-4cb2-8df9-93d014b4d409 고정 할인 바우처 2000원
b18e352a-2d9e-41b5-a851-15a5f044fb11 비율 할인 바우처 25%

명령 메뉴를 입력해주세요 : exit

종료 코드 0(으)로 완료된 프로세스

``` 

```
