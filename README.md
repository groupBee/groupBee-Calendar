# Calendar 📅

## 프로젝트 개요
**groupBee**의 캘린더 서비스는 **FullCalendar**를 활용한 일정 관리 시스템입니다.  
기본적인 캘린더 기능과 **To-do-List**를 제공하며, 회사 내 자산을 예약하는 **예약 서비스**와 연동되어 있습니다.  
또한, **Google Calendar API**를 활용해 **대한민국 공휴일** 정보를 제공하여, 사용자들이 보다 편리하게 일정을 관리할 수 있습니다.

---

## Stacks
<p align="center">
    <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white"/>
    <img src="https://img.shields.io/badge/Google_Calendar_API-4285F4?style=flat-square&logo=google-calendar&logoColor=white"/>
    <img src="https://img.shields.io/badge/JPA_Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring_Cloud_OpenFeign-6DB33F?style=flat-square&logo=spring&logoColor=white"/>
    <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black"/>
    <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white"/>
</p>

---

## Use Case
<p align="center">
    <img src="https://github.com/user-attachments/assets/7728b65d-ac5b-4038-81c0-fb6c92617c8c" width="500"/>
</p>

이 다이어그램은 캘린더 서비스의 **3가지 주요 기능**을 설명합니다.

1. **대한민국 공휴일 출력**
    - **Google Calendar API**를 통해 공휴일 데이터를 받아와 캘린더에 표시.
    - 클릭 시 추가 동작 없음.

2. **사용자 일정 추가/수정/삭제**
    - 사용자가 일정을 추가하고, 클릭하여 **수정** 또는 **삭제**할 수 있음.

3. **Redis Pub/Sub을 통한 예약 관리**
    - 예약 서비스에서 예약 메시지를 전송하면, 캘린더 서비스가 이를 수신하여 차량 및 회의실 예약 정보를 캘린더에 표시.
    - 예약된 항목은 **삭제만** 가능하며, 수정 불가.
    - 예약 시 **차량** 또는 **회의실 모달 창**이 각각 뜸.

---

## 상세 기능 설명

### 1. 공휴일 출력
- **Google Calendar API**를 통해 공휴일 데이터를 실시간으로 동기화.
- 사용자에게 공휴일 정보만 제공, 클릭 시 추가 기능 없음.

### 2. 일정 관리
- 사용자는 일정을 자유롭게 추가/수정/삭제 가능.
- 캘린더에 일정이 실시간으로 반영되며, 직관적인 UI 제공.

### 3. 예약 관리 (Redis Pub/Sub)
- 회사 자산(차량, 회의실) 예약을 관리하는 시스템과 연동.
- 예약이 발생하면 캘린더에 실시간으로 반영, 예약 항목은 삭제만 가능.
