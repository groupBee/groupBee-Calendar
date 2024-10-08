# Calendar 📅

## 프로젝트 개요
**groupBee**의 캘린더 서비스는 직관적이고 효율적인 **FullCalendar**를 기반으로 한 일정 관리 시스템입니다.  
이 서비스는 사용자들이 손쉽게 일정을 추가, 수정, 삭제할 수 있으며, **To-do-List** 기능을 통해 개인과 팀의 일정 관리를 돕습니다.  
또한, 회사 내 자산 예약 시스템과의 연동을 통해 차량 및 회의실 예약을 관리할 수 있습니다.  
특히, **Google Calendar API**를 활용해 **대한민국 공휴일** 정보를 제공, 사용자들이 중요한 날짜를 놓치지 않도록 지원합니다.

---

## 기술 스택

- **Redis**: 실시간 데이터 처리 및 Pub/Sub 기반의 예약 관리에 사용.
- **Google Calendar API**: 대한민국 공휴일을 실시간으로 동기화하여 캘린더에 반영.
- **JPA (Hibernate)**: 데이터베이스와의 상호작용을 최적화하며, 안정적인 데이터 관리를 보장.
- **Spring Cloud OpenFeign**: 서비스 간 통신을 간편하게 처리하고, 외부 API와의 연결을 손쉽게 구현.
- **Swagger**: API 문서화를 통해 직관적인 API 인터페이스 제공.
- **PostgreSQL**: 안정적인 데이터 저장을 위한 고성능 관계형 데이터베이스.

---

## Use Case

<p align="center">
    <img src="https://github.com/user-attachments/assets/7728b65d-ac5b-4038-81c0-fb6c92617c8c" width="500"/>
</p>

이 다이어그램은 캘린더 서비스의 **3가지 핵심 기능**을 설명합니다.

1. **대한민국 공휴일 출력**
    - **Google Calendar API**를 통해 공휴일 데이터를 받아와 캘린더에 실시간으로 표시합니다.
    - 사용자는 공휴일 정보만을 확인할 수 있으며, 클릭해도 추가적인 동작은 발생하지 않습니다.

2. **사용자 일정 관리**
    - 사용자가 직접 일정을 추가하고, 기존 일정을 클릭하여 **수정** 또는 **삭제**할 수 있습니다.
    - 실시간으로 캘린더에 일정이 반영되며, 사용자 경험을 극대화하는 직관적인 UI를 제공합니다.

3. **실시간 예약 관리**
    - **Redis Pub/Sub**을 통해 예약 서비스에서 차량 및 회의실 예약 정보를 수신하여 캘린더에 표시합니다.
    - 예약된 항목은 **삭제**만 가능하며, 수정은 불가능합니다.
    - 예약 시 **차량** 및 **회의실 예약 모달 창**이 각각 표시됩니다.

---

## 상세 기능 설명

### 1. 대한민국 공휴일 출력
- **Google Calendar API**를 통해 실시간으로 대한민국 공휴일 정보를 동기화하여 캘린더에 자동으로 반영합니다.
- 공휴일을 클릭할 때 추가적인 동작 없이 공휴일 정보를 확인할 수 있습니다.

### 2. 일정 관리
- 사용자는 자유롭게 일정을 **추가**, **수정**, **삭제**할 수 있으며, 변경 사항은 즉시 캘린더에 반영됩니다.
- 직관적인 UI를 통해 일정 관리가 간편하며, 일정의 시작과 종료를 한눈에 확인할 수 있습니다.

### 3. 예약 관리 (Redis Pub/Sub)
- **Redis**의 Pub/Sub 기능을 활용하여 차량 및 회의실 예약 정보를 실시간으로 처리합니다.
- 예약된 항목은 **삭제**만 가능하며, 예약 시간이 지난 후에는 모달 창이 초기화됩니다.
- 실시간으로 예약 현황을 관리하고, 회사 자산 예약 시스템과 유기적으로 연동되어 관리 효율을 높입니다.

---

## 결론
이 캘린더 서비스는 일정 관리와 예약 관리의 복합적인 요구를 충족하기 위해 설계된 통합 솔루션입니다.  
**FullCalendar**, **Google Calendar API**, **Redis Pub/Sub**와 같은 최신 기술을 도입하여 사용자 친화적인 경험을 제공합니다.  
또한, **Spring Boot**를 통해 확장성과 안정성을 갖춘 시스템으로, 회사 내 자산 관리와 일정 관리의 핵심적인 부분을 효율적으로 처리할 수 있습니다.
