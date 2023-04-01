package com.team29.backend.controller;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ReduceProduct {
    ArrayList<Long> id;
    ArrayList<Long> quantity;
}
