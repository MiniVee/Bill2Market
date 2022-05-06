import React, {Fragment, useEffect, useState} from "react";
import axios from "axios";
import "./categorySearch.css";
// import store from "../store";
import {Button, Card, Col, Row} from "antd";
import {HeartFilled, HeartOutlined} from "@ant-design/icons";
import HeaderPage from "../header/header";
import ProductListPage from "../productList/productList";
import {useSelector, useDispatch, shallowEqual} from "react-redux";
import {isDisabled} from "@testing-library/user-event/dist/utils";

function CategorySearchPage()  {
    const value = useSelector(state => state.value, shallowEqual)
    const latitude = useSelector(state=> state.latitude)
    const longitude = useSelector(state=> state.longitude)
    const [orderType, setOrderType] = useState('DISTANCE');
    let [page, setPage] = useState(0);
    const [last, setLast] = useState(false);
    const [categoryitemList, setCategoryitemList] = useState([]);

    //더보기시 page값 늘려주는 함수
    const increasePage = () => {
        setPage(++page);
        addCategory(latitude, longitude)
    };

    useEffect(() => {

        onCategory(latitude, longitude)
    }, [value,orderType]);

    //onchange 시에 물품 부르는 함수
    const onCategory = (latitude, longitude) => {

        let url = 'http://localhost:8080/items/search-category?categoryBig='+value[0]

        if(value[1] != undefined && value[1].charAt(1)!='0'){
            url+='&categoryMiddle='+value[1]
        }
        if(value[2] != undefined && value[2].charAt(2)!='0'){
            url+='&categorySmall='+value[2]
        }
        url+='&latitude='+latitude+'&longitude='+longitude+'&orderType='+orderType+'&page=0'
        axios.get( url,
            {headers: {
                    Authorization: 'Bearer ' + sessionStorage.getItem("token")
                }}
            ,
        )
            .then((response) => {
                if (response.status >= 200 && response.status <= 204) {

                    setCategoryitemList(response.data.data.content);
                    setLast(response.data.data.last);


                }
            })
            .catch(res => {

            })

    };
    //더보기 했을때 물품 렌더링 시키는 함수
    const addCategory = (latitude, longitude) => {

        let url = 'http://localhost:8080/items/search-category?categoryBig='+value[0]

        if(value[1] != undefined && value[1].charAt(1)!='0'){
            url+='&categoryMiddle='+value[1]
        }
        if(value[2] != undefined && value[2].charAt(2)!='0'){
            url+='&categorySmall='+value[2]
        }
        url+='&latitude='+latitude+'&longitude='+longitude+'&orderType='+orderType+'&page='+page
        axios.get( url,
            {headers: {
                    Authorization: 'Bearer ' + sessionStorage.getItem("token")
                }}
            ,
        )
            .then((response) => {
                if (response.status >= 200 && response.status <= 204) {

                    setCategoryitemList(categoryitemList.concat(response.data.data.content));
                    setLast(response.data.data.last);


                }
            })
            .catch(res => {

            })

    };

    //찜하기
    const addBasket2 = (itemId,isLike) => { //찜하기가 안된상태에서 찜하기를 눌렀을때

        axios.post("http://localhost:8080/baskets?itemId="+itemId,
            {},{headers: {
                    Authorization: 'Bearer ' + sessionStorage.getItem("token")
                }}
        ).then(response => {
            onCategory(latitude, longitude);
        })
            .catch(error => {
                console.log(error.response);
            })
    }
    const delBasket2 = (itemId,isLike) => { //찜하기가 안된상태에서 찜하기를 눌렀을때

        axios.delete("http://localhost:8080/baskets?itemId="+itemId,
            {headers: {
                    Authorization: 'Bearer ' + sessionStorage.getItem("token")
                }}
        ).then(response => {

            onCategory(latitude, longitude);
        })
            .catch(error => {

                console.log(error.response);
            })
    }

    //날짜 포맷
    function format  (date) {

        return date.getFullYear() + "년 " + date.getMonth() + "월 " + date.getDate() + "일 " + date.getHours() + "시" ;

    }
    //거리순, 고가순, 저가순, 최신순 버튼함수
    const changeOrderType = () => {
        setOrderType('DISTANCE')
        setCategoryitemList([])
        setPage(0)

        // onCategory(latitude, longitude)
    }
    const changeOrderExType = () => {
        setOrderType('EXPENSIVE')
        setCategoryitemList([])
        setPage(0)


    }
    const changeOrderInExType = () => {
        setOrderType('INEXPENSIVE')
        setCategoryitemList([])
        setPage(0)


    }
    const changeOrderReType = () => {
        setOrderType('RECENTLY')
        setCategoryitemList([])
        setPage(0)

    }

    return (

        <Fragment>

                <div className="row">
                    <div className="itemSearch">

                        <div className="itemSearchResult">

                        </div>

                        <div className="itemSort">
                            {orderType === 'DISTANCE'?
                            <Button className="distance" disabled >거리순</Button>:
                                <Button className="distance" onClick={changeOrderType} >거리순</Button>
                            }
                            {orderType === 'RECENTLY'?
                            <Button className="recent" disabled>최신순</Button>:
                                <Button className="recent" onClick={changeOrderReType}>최신순</Button>
                            }
                            {orderType === "EXPENSIVE"?
                            <Button className="lowPrice" disabled>고가순</Button>:
                                <Button className="lowPrice" onClick={changeOrderExType}>고가순</Button>
                            }
                            {orderType === "INEXPENSIVE"?
                            <Button className="highPrice" disabled>저가순</Button>:
                                <Button className="highPrice" onClick={changeOrderInExType}>저가순</Button>
                            }


                        </div>

                    </div>
                    <Row  gutter={24}>

                        {categoryitemList.map(categoryitem => {


                            return (

                                <Col span={4.5} className="col">
                                    <Card  hoverable
                                           key={categoryitem.itemId} className="cards">


                                        {categoryitem.isLike?

                                            <HeartFilled onClick ={()=>{delBasket2(categoryitem.itemId,categoryitem.isLike)
                                            }}></HeartFilled>:
                                            <HeartOutlined onClick ={()=>addBasket2(categoryitem.itemId,categoryitem.isLike)}></HeartOutlined>
                                        }

                                        <span> <h2 className="title"> 제목: {categoryitem.itemTitle}</h2>
                                            {categoryitem.contractStatus === "GENERAL" ?
                                                <p ></p>:
                                                categoryitem.contractStatus === "RENTAL" ?
                                                    <p className="rental">대여중</p>:
                                                    <p className="reservation">예약중</p>}</span>
                                        <p>게시일: {format(new Date(categoryitem.createDate))}</p>
                                        <p>대여료: {categoryitem.price}</p>
                                        <p>보증금: {categoryitem.deposit}</p>
                                        <p>아이템 위치: {categoryitem.itemAddress}</p>


                                        <img className="phoneImage" src={categoryitem.itemPhoto}/>



                                    </Card>
                                </Col>
                            )
                        })}
                    </Row>

                </div>
            {last === true?
                <Button className="addButton" disabled>더보기</Button>:
                <Button className="addButton" onClick={increasePage}>더보기</Button>
            }




        </Fragment>
    )


}

;
export default CategorySearchPage;
