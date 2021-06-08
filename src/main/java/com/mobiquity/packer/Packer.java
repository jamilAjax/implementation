package com.mobiquity.packer;

import com.mobiquity.entities.Data;
import com.mobiquity.exception.APIException;
import com.mobiquity.service.SolutionConcrete;
import com.mobiquity.service.SolutionInterface;
import com.mobiquity.formatter.ListFormatter;
import com.mobiquity.formatter.NewLineFormatter;
import com.mobiquity.parser.Parser;

import java.util.List;
import java.util.stream.Collectors;

public class Packer {

  private Packer() {
  }

  public static String pack(String filePath) throws APIException {
    SolutionInterface service = new SolutionConcrete();
    ListFormatter formatter = new NewLineFormatter();

    List<Data> items = Parser.getInstance().parse(filePath);

    List<String> solutions = items.stream()
            .map(service::getOptimalItemIdsInString)
            .collect(Collectors.toList());

    return formatter.format(solutions);
  }
}
